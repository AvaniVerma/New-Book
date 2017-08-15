package com.avani.newbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private Button login;
    private TextView signup_link;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;



    public void userLogin()
    {
        String email=mEmail.getText().toString().trim();
        String password=mPassword.getText().toString().trim();
        progressDialog=new ProgressDialog(this);
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password))
        {
            progressDialog.setMessage("Logging in....."); progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.hide();
                        finish();
                        startActivity(new Intent(Login.this, MyBookList.class));
                    }
                    else
                    {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), "Error logging in....Please try again !!", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please fill all the details..", Toast.LENGTH_SHORT).show();
        }

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail=(EditText) findViewById(R.id.email_field);
        mPassword=(EditText) findViewById(R.id.password_field);
        login=(Button) findViewById(R.id.login_btn);
        signup_link=(TextView) findViewById(R.id.loginLink);
        mAuth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });



     signup_link.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             finish();
             startActivity(new Intent(Login.this, Register.class));
         }
     });

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


 /*   @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    */
}
