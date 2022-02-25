package com.nova.cstorage.book;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;


    public class Activity_book_data implements Serializable {

        private byte[] book_pic;
        private String bookname;
        private String bookauth;
        private String bookwrite;


        public Activity_book_data(String bookname, String bookauth, String bookwrite,byte[] book_pic) {

            this.book_pic = book_pic;
            this.bookname = bookname;
            this.bookauth = bookauth;
            this.bookwrite = bookwrite;
            this.book_pic = book_pic;
        }


        public byte[] getBook_pic() {

            return book_pic;
        }

        public void setBook_pic(byte[] book_pic) {

            this.book_pic = book_pic;
        }

        public String getBookname() {

            return bookname;
        }

        public void setBookname(String bookname) {

            this.bookname = bookname;
        }

        public String getBookauth() {

            return bookauth;
        }

        public void setBookauth(String bookauth) {

            this.bookauth = bookauth;
        }

        public String getBookwrite() {

            return bookwrite;
        }

        public void setBookwrite(String bookwrite) {

            this.bookwrite = bookwrite;
        }
    }

