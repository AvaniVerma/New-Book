package com.avani.newbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {


    private EditText mEmail, mPassword, mName,mConfirmPassword;
    private Button registerButton;
    private TextView loginLink;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;



    public void registerUser()
    {
        final String email=mEmail.getText().toString().trim();
        final String name=mName.getText().toString().trim();
        String confirmPassword=mConfirmPassword.getText().toString().trim();
        String password=mPassword.getText().toString().trim();
        progressDialog=new ProgressDialog(this);
        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(confirmPassword))
        {
            if(password.equals(confirmPassword))
            {
                progressDialog.setMessage("Registering....."); progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            String user_id=mAuth.getCurrentUser().getUid();
                            DatabaseReference user_db= mDatabase.child(user_id);
                            user_db.child("name").setValue(name);
                            user_db.child("email").setValue(email);

                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT). show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MyBookList.class));
                        }
                        else
                        {
                            progressDialog.hide();
                            Toast.makeText(Register.this, "Error..Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else
            {

                Toast.makeText(Register.this, "Your passwords do not match", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName=(EditText)findViewById(R.id.registerDisplayName);
        mEmail=(EditText) findViewById(R.id.email_field);
        mPassword=(EditText) findViewById(R.id.password_field);
        mConfirmPassword=(EditText) findViewById(R.id.confirmPassword_field);
        registerButton=(Button) findViewById(R.id.register_btn);
        loginLink=(TextView) findViewById(R.id.login);
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");


        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent =new Intent(getApplicationContext(), Login.class);
            finish();
            startActivity(intent);
        }


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });




    }


}
