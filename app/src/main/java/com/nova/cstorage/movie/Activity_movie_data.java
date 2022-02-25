package com.nova.cstorage.movie;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;


public class Activity_movie_data implements Serializable {

    private byte[] movie_pic;
    private String movieName;
    private String movieDiretor;
    private String moviewrite;


    public Activity_movie_data(String moviename, String movieauth, String moviewrite,byte[] movie_pic) {

        this.movie_pic = movie_pic;
        this.movieName = moviename;
        this.movieDiretor = movieauth;
        this.moviewrite = moviewrite;
        this.movie_pic = movie_pic;
    }


    public byte[] getMovie_pic() {

        return movie_pic;
    }

    public void setMovie_pic(byte[] movie_pic) {

        this.movie_pic = movie_pic;
    }

    public String getMoviename() {

        return movieName;
    }

    public void setMoviename(String moviename) {

        this.movieName = moviename;
    }

    public String getMovieauth() {

        return movieDiretor;
    }

    public void setMovieauth(String movieauth) {

        this.movieDiretor = movieauth;
    }

    public String getMoviewrite() {

        return moviewrite;
    }

    public void setMoviewrite(String moviewrite) {

        this.moviewrite = moviewrite;
    }
}

