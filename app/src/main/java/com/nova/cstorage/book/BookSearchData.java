package com.nova.cstorage.book;

public class BookSearchData {
    private String title;
    private String author;

    public BookSearchData(String title, String author) {

//        this.book_pic = book_pic;
        this.title = title;
        this.author = author;
    }
        public String getTitle () {
            return title;
        }

        public void setTitle (String title){
            this.title = title;
        }

        public String getAuthor () {
            return author;
        }

        public void setAuthor (String author){
            this.author = author;
        }

//    public String getBloggername() {
//        return bloggername;
//    }
//
//    public void setBloggername(String bloggername) {
//        this.bloggername = bloggername;
//    }


    }
