package com.avani.newbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MyBookList extends AppCompatActivity {

    private RecyclerView mBookList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_list);

        mBookList=(RecyclerView) findViewById(R.id.book_list);
        mBookList.setHasFixedSize(true);
        mBookList.setLayoutManager(new LinearLayoutManager(this));
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("User Book").child(mAuth.getCurrentUser().getUid());

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Book, BookViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Book, BookViewHolder>(
                Book.class,
                R.layout.book_row,
                BookViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(BookViewHolder viewHolder, Book model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setAuthor(model.getAuthor());
                viewHolder.setGenre(model.getGenre());
                viewHolder.setImageUri(getApplicationContext(), model.getImageUri());
            }
        };
        mBookList.setAdapter(firebaseRecyclerAdapter);

    }



    public static class BookViewHolder extends RecyclerView.ViewHolder {

        View mView;


        public BookViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title) {
            TextView book_title = (TextView) mView.findViewById(R.id.book_title);
            book_title.setText(title);
        }

        public void setAuthor(String author) {
            TextView author_name = (TextView) mView.findViewById(R.id.book_author);
            author_name.setText(author);
        }

        public void setGenre(String genre) {
            TextView genre_name = (TextView) mView.findViewById(R.id.book_genre);
            genre_name.setText(genre);
        }

        public void setImageUri(Context ctx, String imageUri) {
            ImageView book_Image=(ImageView) mView.findViewById(R.id.book_image);
            Picasso.with(ctx).load(imageUri).into(book_Image);
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.addIcon)
        {  // finish();
            startActivity(new Intent(MyBookList.this, AddBook.class));}

        if(item.getItemId()==R.id.searchIcon) {
           // finish();
            startActivity(new Intent(MyBookList.this, Search.class));
        }

        if(item.getItemId()==R.id.profileIcon) {

           // finish();
            startActivity(new Intent(MyBookList.this, Profile.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
