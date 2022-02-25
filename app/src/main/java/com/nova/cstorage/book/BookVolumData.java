package com.nova.cstorage.book;

public class BookVolumData {
    private int bookVolum;
    private String bookTitle;


    public BookVolumData(int bookVolum, String bookTitle) {

        this.bookVolum = bookVolum;
        this.bookTitle = bookTitle;
    }

    public int getBookVolum() {
        return bookVolum;
    }

    public void setBookVolum(int bookVolum) {
        this.bookVolum = bookVolum;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}