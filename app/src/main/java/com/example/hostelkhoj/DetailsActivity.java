package com.example.hostelkhoj;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference ref,reference;
    DocumentReference documentReference;
    FirebaseFirestore fstore;
    TextView hostelnametxt,contacttxt,ownertxt,email,available,description,type,area;
    String uid;
    double latt,longg;
    ImageSlider imageSlider;
    List<SlideModel> imglist;
    Geocoder geocoder;
    List<Address> addresses;
    Address address;
    String locality;
    Button seeloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        storage=FirebaseStorage.getInstance();
        setContentView(R.layout.activity_details);
        Bundle bundle=getIntent().getExtras();
        uid=bundle.getString("user");
        Log.d("TAG","uid: "+uid);
        ref=storage.getReference();
        fstore=FirebaseFirestore.getInstance();
        documentReference=fstore.collection("users").document(uid);
        geocoder=new Geocoder(this,Locale.getDefault());

        address = null;

        imageSlider=findViewById(R.id.image_slider);
        hostelnametxt=findViewById(R.id.hostelname);
        contacttxt=findViewById(R.id.contact);
        ownertxt=findViewById(R.id.Ownername);
        email=findViewById(R.id.email);
        available=findViewById(R.id.availablecots);
        description=findViewById(R.id.description);
        type=findViewById(R.id.hosteltype);
        area=findViewById(R.id.area);
        seeloc=findViewById(R.id.openmapsbt);

        imglist=new ArrayList<SlideModel>();

        setSLider();
        fillUI();

        seeloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?q="+latt+","+longg+"(My+Point)&z=14&ll="+latt+","+longg;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });



    }

    private void fillUI() {
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {
                    latt = value.getDouble("loclattitude");
                    longg=value.getDouble("loclongitude");
                    try {
                        addresses=geocoder.getFromLocation(latt,longg,1);
                        address=addresses.get(0);
                        locality=address.getSubLocality();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    hostelnametxt.setText(value.getString("hostelname") + " HOSTEL");
                    ownertxt.setText("OWNER NAME : " + value.getString("ownername"));
                    contacttxt.setText("CONTACT NO : " + value.getString("contact"));
                    type.setText("TYPE : " + value.getString("type"));
                    available.setText("COTS AVAILABLE : " + value.getString("Availablecots"));
                    description.setText(value.getString("description"));
                    email.setText("EMAIL : " + value.getString("email"));
                    if (locality!=null)
                        area.setText("AREA : "+locality);
                    else
                        area.setText("AREA : Not Specified ");

                }
            }
        });

    }

    public void setSLider()     //this method calls the firebase image urls and loads the slider with that images
    {
        //getting image 1 from firebase
        reference=ref.child("hostelimages/users/"+uid+"/img1");
        Log.d("TAG","getting uri1");
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("TAG","got uri1");
                imglist.add(new SlideModel(uri.toString(),"IMAGES",ScaleTypes.CENTER_CROP));

                //getting image 2 from firebase
                reference=ref.child("hostelimages/users/"+uid+"/img2");
                Log.d("TAG","getting uri2");
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("TAG","got uri2");
                        imglist.add(new SlideModel(uri.toString(),"IMAGES",ScaleTypes.CENTER_CROP));

                        //getting image 3 from firebase
                        reference=ref.child("hostelimages/users/"+uid+"/img3");
                        Log.d("TAG","getting uri3");
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("TAG","got uri3");
                                imglist.add(new SlideModel(uri.toString(),"IMAGES",ScaleTypes.CENTER_CROP));

                                //getting image 4 from firebase
                                reference=ref.child("hostelimages/users/"+uid+"/img4");
                                Log.d("TAG","getting uri4");
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imglist.add(new SlideModel(uri.toString(),"IMAGES",ScaleTypes.CENTER_CROP));
                                        imageSlider.setImageList(imglist);
                                    }
                                });

                            }
                        });

                    }
                });
            }
        });
    }

}
