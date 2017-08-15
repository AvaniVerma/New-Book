package com.avani.newbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private Button signout;
    private TextView nameDisplay, contact;
    private ImageView userImage;
    private static final int Gallery_Intent=2;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        signout=(Button) findViewById(R.id.signout_btn);
        mAuth=FirebaseAuth.getInstance();
        userImage=(ImageView) findViewById(R.id.displayImage);
        user=mAuth.getCurrentUser();
        nameDisplay=(TextView) findViewById(R.id.displayName);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("name");





        // If user clicks on display pic, takes him to gallery to choose a new one
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_Intent );
            }
        });

        // Signs out the user and fires up the login activity
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String found=dataSnapshot.getValue().toString().trim();
                nameDisplay.setText(found);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        contact=(TextView) findViewById(R.id.contactInfo);
        contact.setText(mAuth.getCurrentUser().getEmail());

        if(mAuth.getCurrentUser().getPhotoUrl()!=null)
            userImage.setImageURI(mAuth.getCurrentUser().getPhotoUrl());


    }





    // Updates a user's profile photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Intent && resultCode == RESULT_OK) {
            final Uri uri = data.getData();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  //  userImage.setImageURI(null);
                    userImage.setImageURI(uri);
                    Toast.makeText(Profile.this, "Profile photo updated successfully.", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }


}
