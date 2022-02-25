package com.nova.cstorage.exhibition;

import java.io.Serializable;

public class Activity_exhibition_data implements Serializable {

    private byte[] exhibition_pic;
    private String exhibition_name;
    private String exhibition_auth;
    private String exhibition_write;


    public Activity_exhibition_data(String exhibitionname, String exhibitionauth, String exhibitionwrite,byte[] exhibition_pic) {

        this.exhibition_name = exhibitionname;
        this.exhibition_auth = exhibitionauth;
        this.exhibition_write = exhibitionwrite;
        this.exhibition_pic = exhibition_pic;

    }


    public byte[] getExhibition_pic() {

        return exhibition_pic;
    }

    public void setExhibition_pic(byte[] exhibition_pic) {

        this.exhibition_pic = exhibition_pic;
    }

    public String getExhibitionname() {

        return exhibition_name;
    }

    public void setExhibitionname(String exhibitionname) {

        this.exhibition_name = exhibitionname;
    }

    public String getExhibitionauth() {

        return exhibition_auth;
    }

    public void setExhibitionauth(String exhibitionauth) {

        this.exhibition_auth = exhibitionauth;
    }

    public String getExhibitionwrite() {

        return exhibition_write;
    }

    public void setexhibitionWrite(String exhibitionwrite) {

        this.exhibition_write = exhibitionwrite;
    }
}

