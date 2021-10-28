package com.example.hostelkhoj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
Uri imguri1,imguri2,imguri3,imguri4;
EditText hostelname,contactno,cots,description;
ImageView img1,img2,img3,img4;
Button savebtn;
FirebaseStorage storage;
FirebaseAuth fauth;
FirebaseUser user;
FirebaseFirestore fstore;
StorageReference sreference,imageref;
String userId;
String email;
ProgressDialog pd;
    int cnt=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img1=findViewById(R.id.imgview1);
        img2=findViewById(R.id.imgview2);
        img3=findViewById(R.id.imgview3);
        img4=findViewById(R.id.imgview4);
        savebtn=findViewById(R.id.savebtn);
        hostelname=findViewById(R.id.editTextHostelName);
        contactno=findViewById(R.id.editTextcontactnumber);
        cots=findViewById(R.id.editTextcots);
        description=findViewById(R.id.descriptionedit);

        //firebase initialization
        storage=FirebaseStorage.getInstance();
        sreference=storage.getReference();
        fauth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        user=fauth.getCurrentUser();
        userId=user.getUid();

        getImagefromfirebase(); //method to get images from firebase at starting
        getData();//method retrives data and gets in edit texts
        savebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(MainActivity.this);
                pd.setTitle("Uploading Images");
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setProgress(0);
                uploadimages(imguri1,imguri2,imguri3,imguri4);//uploading images to firebase
                updateInfo();

    }
        });
    }

    private void updateInfo() {
        DocumentReference ref=fstore.collection("users").document(userId);
        Map<String,Object> update=new HashMap<>();
        update.put("hostelname",hostelname.getText().toString());
        update.put("contact",contactno.getText().toString());
        update.put("Availablecots",cots.getText().toString());
        update.put("description",description.getText().toString());
        ref.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this,"Info updated!",Toast.LENGTH_SHORT);
            }
        });
    }

    //this method retrives the data from firebase and fills it in text box
    private void getData() {
        DocumentReference ref=fstore.collection("users").document(userId);
        ref.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    hostelname.setText(value.getString("hostelname"));
                    contactno.setText(value.getString("contact"));
                    cots.setText(value.getString("Availablecots"));
                    description.setText(value.getString("description"));
                }
                else
                    Toast.makeText(MainActivity.this,"User doesnot exists!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //this method uploads images to firebase
    private void uploadimages(Uri imguri1, Uri imguri2, Uri imguri3, Uri imguri4) {

        if(imguri1==null)
            cnt--;
        if(imguri2==null)
            cnt--;
        if(imguri3==null)
            cnt--;
        if(imguri4==null)
            cnt--;
        pd.setMax(cnt);
        if (cnt>0) {
            pd.show();
            //pd.dismiss();
        }
        StorageReference riversRef;
        if(imguri1!=null) {
           riversRef = sreference.child("hostelimages/users/" + fauth.getCurrentUser().getUid() + "/img1");
            riversRef.putFile(imguri1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //Toast.makeText(MainActivity.this,"updated",Toast.LENGTH_SHORT).show();
                            Log.d("tag", "uploaded first msg");
                            pd.incrementProgressBy(1);
                            if(cnt==pd.getProgress())
                                pd.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            //Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {


                        }
                    });
        } //uploads first image if it is changed
        if(imguri2!=null) {
            riversRef = sreference.child("hostelimages/users/" + fauth.getCurrentUser().getUid() + "/img2");
            riversRef.putFile(imguri2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.d("tag", "uploaded first msg");
                            pd.incrementProgressBy(1);
                            if(cnt==pd.getProgress())
                                pd.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {


                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {


                        }
                    });
        } //uploads second image if it is changed
        if(imguri3!=null) {
            riversRef = sreference.child("hostelimages/users/" + fauth.getCurrentUser().getUid() + "/img3");
            riversRef.putFile(imguri3)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //Toast.makeText(MainActivity.this,"updated",Toast.LENGTH_SHORT).show();
                            pd.incrementProgressBy(1);
                            if(cnt==pd.getProgress())
                                pd.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            //Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {


                        }
                    });
        } //uploads third image if it is changed
        if(imguri4!=null) {
            riversRef = sreference.child("hostelimages/users/" + fauth.getCurrentUser().getUid() + "/img4");
            riversRef.putFile(imguri4)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            pd.incrementProgressBy(1);
                            if(cnt==pd.getProgress())
                                pd.dismiss();
                            Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        }
                    });
        } //uploads forth image if it is changed
    }

    //this method gets the images from firebase
    private void getImagefromfirebase() {
        imageref=sreference.child("hostelimages/users/"+fauth.getCurrentUser().getUid()+"/img1");
        imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(img1);
            }
        });

        imageref=sreference.child("hostelimages/users/"+fauth.getCurrentUser().getUid()+"/img2");
        imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(img2);
            }
        });

        imageref=sreference.child("hostelimages/users/"+fauth.getCurrentUser().getUid()+"/img3");
        imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(img3);
            }
        });

        imageref=sreference.child("hostelimages/users/"+fauth.getCurrentUser().getUid()+"/img4");
        imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(img4);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            imguri1=data.getData();
            Log.d("TAG", " "+imguri1);
            img1.setImageURI(imguri1);

        }
        if (requestCode==2&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            imguri2=data.getData();
            img2.setImageURI(imguri2);
        }
        if (requestCode==3&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            imguri3=data.getData();
            img3.setImageURI(imguri3);
        }
        if (requestCode==4&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            imguri4=data.getData();
            img4.setImageURI(imguri4);
        }
    }

    public void image1select(View view) {
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,1);
    }

    public void image3select(View view) {
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,3);
    }

    public void image4select(View view) {
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,4);
    }

    public void logoutbtn(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),login.class));
        finish();
    }

    public void image2select(View view) {
        Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this,login.class));
        finish();
    }
}
