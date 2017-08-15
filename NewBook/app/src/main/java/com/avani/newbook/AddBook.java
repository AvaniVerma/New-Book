package com.avani.newbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddBook extends AppCompatActivity {


    private ImageButton mSelectImage;
    private EditText mTitle, mAuthor, mGenre;
    private Button mAddBookBtn;
    private Uri imageUri=null;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private static final int GALLERY_REQUEST=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        mSelectImage=(ImageButton) findViewById(R.id.addImage);
        mTitle=(EditText) findViewById(R.id.bookTitle);
        mAuthor=(EditText) findViewById(R.id.author);
        mGenre=(EditText) findViewById(R.id.genre);
        mAddBookBtn=(Button) findViewById(R.id.addBook);
        mAuth=FirebaseAuth.getInstance();
        mStorage= FirebaseStorage.getInstance().getReference();
        mProgress=new ProgressDialog(this);
        mDatabase= FirebaseDatabase.getInstance().getReference();


        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });



        mAddBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAdding();
            }
        });

    }



    private void startAdding()
    {
        final String title_val=mTitle.getText().toString().trim();//.substring(0,1).toUpperCase()+mTitle.getText().toString().trim().substring(1).toLowerCase();
        final String author_val=mAuthor.getText().toString().trim();//.substring(0,1).toUpperCase()+mAuthor.getText().toString().trim().substring(1).toLowerCase();
        final String genre_val=mGenre.getText().toString().trim();//.substring(0,1).toUpperCase()+mGenre.getText().toString().trim().substring(1).toLowerCase();
        mProgress.setMessage("Adding book to your records");

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(author_val) && !TextUtils.isEmpty(genre_val) && imageUri!=null)
        {

                final String final_title=title_val.substring(0,1).toUpperCase()+title_val.substring(1).toLowerCase();

                 mProgress.show();
                StorageReference filePath=mStorage.child("Book Images").child(mAuth.getCurrentUser().getUid()).
                                            child(final_title);
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl=taskSnapshot.getDownloadUrl();
                        DatabaseReference newBook=mDatabase.child("User Book").child(mAuth.getCurrentUser().getUid()).child(final_title);
                        newBook.child("title").setValue(final_title);
                        newBook.child("author").setValue(author_val);
                        newBook.child("genre").setValue(genre_val);
                        newBook.child("imageUri").setValue(downloadUrl.toString());

                        mProgress.setMessage("New");
                        DatabaseReference newRef=mDatabase.child("Books").child(final_title);
                        newRef.push().setValue(mAuth.getCurrentUser().getEmail());

                        mProgress.dismiss();
                        Toast.makeText(AddBook.this, "Book added successfully to your records. ", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddBook.this, MyBookList.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                           mProgress.dismiss();
                            Toast.makeText(AddBook.this, "Error in adding book to your records.Please try again. ", Toast.LENGTH_SHORT).show();
                    }
                });
        }
        else
        {
            Toast.makeText(AddBook.this, "Please enter all the details. ", Toast.LENGTH_SHORT).show();
        }


    }




/*    @Override
    public void onBackPressed() {
        super.onBackPressed();

            finish();
            startActivity(new Intent(AddBook.this, MyBookList.class));

    }
*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST&&resultCode==RESULT_OK)
        {
            imageUri=data.getData();
            mSelectImage.setImageURI(imageUri);
        }


    }
}
