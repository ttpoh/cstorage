package com.nova.cstorage.concert;

import java.io.Serializable;

public class Activity_concert_data implements Serializable {

    private byte[] concert_pic;
    private String concertname;
    private String concertauth;
    private String concertwrite;


    public Activity_concert_data(String concertname, String concertauth, String concertwrite,byte[] concert_pic) {

        this.concert_pic = concert_pic;
        this.concertname = concertname;
        this.concertauth = concertauth;
        this.concertwrite = concertwrite;
        this.concert_pic = concert_pic;
    }


    public byte[] getConcert_pic() {

        return concert_pic;
    }

    public void setConcert_pic(byte[] concert_pic) {

        this.concert_pic = concert_pic;
    }

    public String getConcertName() {

        return concertname;
    }

    public void setConcertName(String concertname) {

        this.concertname = concertname;
    }

    public String getConcertAuth() {

        return concertauth;
    }

    public void setConcertAuth(String concertauth) {

        this.concertauth = concertauth;
    }

    public String getConcertWrite() {

        return concertwrite;
    }

    public void setConcertWrite(String concertwrite) {

        this.concertwrite = concertwrite;
    }
}

