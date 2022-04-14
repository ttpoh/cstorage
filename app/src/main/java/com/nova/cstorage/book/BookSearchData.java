package com.nova.cstorage.book;

import java.io.Serializable;

public class BookSearchData implements Serializable {
    private String title;
    private String author;
    private String book_pic;

    public BookSearchData(){

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

    public String getBookpic() {
        return book_pic;
    }

    public void setBook_pic(String book_pic) {
        this.book_pic = book_pic;
    }


}
