package com.example.hostelkhoj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.NetworkErrorException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    TextView register,forget;
    Button login;
    EditText username,password;
    FirebaseAuth fAuth;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.editTextusername);
        password=findViewById(R.id.editTextpassword);
        login=findViewById(R.id.loginbtn);
        register=findViewById(R.id.register);
        forget=findViewById(R.id.forget);
        progress=findViewById(R.id.progressBar);

        fAuth=FirebaseAuth.getInstance();
        Sprite doubleBounce= new FadingCircle();
        progress.setIndeterminateDrawable(doubleBounce);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    String email, pass;
                    email = username.getText().toString().trim();
                    pass = password.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        username.setError("Enter valid email ");
                        return;
                    }
                    if (TextUtils.isEmpty(pass)) {
                        password.setError("Please enter password");
                        return;
                    }

                    progress.setVisibility(View.VISIBLE);
                    fAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(com.example.hostelkhoj.login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(com.example.hostelkhoj.login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(com.example.hostelkhoj.login.this," Login Unuccessful "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("TAG","loginunsuccesful");
                            progress.setVisibility(View.GONE);
                        }
                    });
                }
                catch(Exception e)
                {
                    Toast.makeText(login.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }


    public void registerbtn(View view) {
        Intent i=new Intent(login.this, register.class);
        startActivity(i);
    }

    public void forgetpassbtn(View view) {
        final EditText resetmail=new EditText(view.getContext());
        AlertDialog.Builder passwordresetdialog=new AlertDialog.Builder(view.getContext());
        passwordresetdialog.setTitle("Reset Password");
        passwordresetdialog.setMessage("ENter your registered email");
        passwordresetdialog.setView(resetmail);
        passwordresetdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mail=resetmail.getText().toString();
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(com.example.hostelkhoj.login.this,"Reset link sent to your email ",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this,"Reset Link not sent Error!",Toast.LENGTH_SHORT).show();
                        Log.e("TAG",e.getMessage());
                    }
                });
            }
        });

        passwordresetdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        passwordresetdialog.create().show();
    }

    public void skip(View view) {
        Intent i=new Intent(com.example.hostelkhoj.login.this,HomePage.class);
        startActivity(i);
        finish();
    }
}
