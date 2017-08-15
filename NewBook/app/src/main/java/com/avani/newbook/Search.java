package com.avani.newbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ListView mUserList;
    private TextView mBookTitle;
    private Button mButton;
    private ArrayList<String> mUsernames=new ArrayList<>();
    DatabaseReference newRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        mDatabase= FirebaseDatabase.getInstance().getReference().child("Books");
        mUserList=(ListView) findViewById(R.id.bookList);
        mBookTitle=(TextView) findViewById(R.id.searchText);
        mButton=(Button) findViewById(R.id.searchButton);



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsernames.clear();
                searchFor();
            }
        });

    }


  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        startActivity(new Intent(Search.this, MyBookList.class));

    }
    */



    private void searchFor() {

       final String bookName=mBookTitle.getText().toString().trim();
        if(!TextUtils.isEmpty(bookName))
        {
            final String book_val=bookName.substring(0,1).toUpperCase()+bookName.substring(1).toLowerCase();
            newRef=mDatabase.child(book_val);
            newRef.child("Null").setValue(" ");
            final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUsernames);
            mUserList.setAdapter(arrayAdapter);


            newRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    String value=dataSnapshot.getValue(String.class);
                    mUsernames.add(value);
                    arrayAdapter.notifyDataSetChanged();
                //    Toast.makeText(Search.this, "Doing something", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }
        else{
            Toast.makeText(Search.this, "Please enter a book name", Toast.LENGTH_SHORT).show();
        }

    }


}
