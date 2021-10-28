package com.example.hostelkhoj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class register extends AppCompatActivity {

    public static final String TAG= "tag";
    EditText hostelname,ownername,contactno,emailid,password,hosteladdress;
    String hname,oname,contact,email,uname,pass,haddress;
    Button register;
    RadioButton boys,girls,coed;
    RadioGroup type;
    TextView loginbtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String Uid;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hostelname=findViewById(R.id.editTextHostelName);
        ownername=findViewById(R.id.editTextownername);
        contactno=findViewById(R.id.editTextcontactnumber);
        emailid=findViewById(R.id.editTextemailid);
        password=findViewById(R.id.editTextpassword);
        hosteladdress=findViewById(R.id.editTextaddress);
        boys=findViewById(R.id.boysradio);
        girls=findViewById(R.id.girlsradio);
        coed=findViewById(R.id.coedradio);
        type=findViewById(R.id.typeradio);

        register=findViewById(R.id.registerbtn);

        loginbtn=findViewById(R.id.textViewlogin);
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        geocoder=new Geocoder(this,Locale.getDefault());
        Log.d(TAG,"Initialized");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hname=hostelname.getText().toString().trim();
                oname=ownername.getText().toString().trim();
                contact=contactno.getText().toString().trim();
                email=emailid.getText().toString().trim();
                pass=password.getText().toString().trim();
                haddress=hosteladdress.getText().toString().trim();


                if(setError()) {
                    Log.d(TAG, "Error checked");


                    if (fAuth.getCurrentUser() != null) {
                        startActivity(new Intent(getApplicationContext(), login.class));
                        finish();
                    }
                    Log.d(TAG, " Data entered");


                    fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "user created");
                                List<Address> addresses;
                                Address address = null;
                                double lattitude = 0, longitude = 0;


                                Toast.makeText(com.example.hostelkhoj.register.this, "Regirstered Succesfully", Toast.LENGTH_SHORT).show();
                                Uid = fAuth.getCurrentUser().getUid();
                                try {
                                    addresses = geocoder.getFromLocationName(haddress, 1);
                                    if (!addresses.isEmpty())
                                        address = addresses.get(0);
                                    else {
                                        address = new Address(new Locale("en", "India"));
                                        Toast.makeText(register.this, "Cannot find specified location ", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.d(TAG, address.toString());
                                    lattitude = address.getLatitude();
                                    longitude = address.getLongitude();
                                    Log.d(TAG, "geolocated");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(register.this, "Cannot find specified location ", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "geolocated exception");
                                }

                                DocumentReference docreference = fStore.collection("users").document(Uid);
                                Map<String, Object> user = new HashMap<>();
                                user.put("hostelname", hname);
                                user.put("ownername", oname);
                                user.put("contact", contact);
                                user.put("email", email);
                                user.put("password", pass);

                                user.put("loclattitude", lattitude);
                                user.put("loclongitude", longitude);
                                Log.d(TAG, "getting type");
                                user.put("type", getType());
                                Log.d(TAG, "radio info got");
                                user.put("Availablecots", "0");
                                user.put("description", " ");
                                docreference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "completed");
                                    }
                                });


                                startActivity(new Intent(com.example.hostelkhoj.register.this, login.class));
                                finish();
                            } else {
                                Toast.makeText(com.example.hostelkhoj.register.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Failure " + e.toString());
                        }
                    });
                }
            }
        });


    }

    //this method sets the error if something is not given or not validated
    private boolean setError() {
        if(TextUtils.isEmpty(hname))
        {
            hostelname.setError("Hostel Name is required ");
            return false;
        }
        if(TextUtils.isEmpty(oname))
        {
            ownername.setError("Owner Name is required ");
            return false;
        }
        if(TextUtils.isEmpty(contact))
        {
            contactno.setError("Owner Name is required ");
            return false;
        }
        if(TextUtils.isEmpty(email))
        {
            emailid.setError("Owner Name is required ");
            return false;
        }

        if(TextUtils.isEmpty(pass))
        {
            password.setError("Owner Name is required ");
            return false;
        }
        if(pass.length()<6)
        {
            password.setError("Password should be more than 8 characters");
            return false;
        }

        if(TextUtils.isEmpty(haddress))
        {
            hosteladdress.setError("Hostel address is required ");
            return false;
        }
        return true;
    }

    //this method returns the selected item of radio button
    private String getType() {

        if(type.getCheckedRadioButtonId()==R.id.boysradio)
           return "boys";
        else if (type.getCheckedRadioButtonId()==R.id.girlsradio)
            return "girls";
        else if(type.getCheckedRadioButtonId()==R.id.coedradio)
            return "coed";
        else
            return "not specified";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void loginbtn(View view) {
        Intent i=new Intent(register.this,login.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(com.example.hostelkhoj.register.this,login.class));

    }
}

