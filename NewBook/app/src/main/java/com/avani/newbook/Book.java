package com.avani.newbook;

/**
 * Created by Avani on 4/28/2017.
 */

public class Book {

    private String title, author, genre, imageUri;

    public Book()
    {

    }


    public Book(String title, String author, String genre, String imageUri) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.imageUri = imageUri;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
