package com.nova.cstorage.book;

public class BookRankData {

    public int _id;
    public int bookRank;
    public String bookTitle;

    public int getBookRank() {
        return bookRank;
    }

    public void setBookRank(int bookRank) {
        this.bookRank = bookRank;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
