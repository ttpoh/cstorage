package com.nova.cstorage.etc;

import java.io.Serializable;

public class Activity_etc_data implements Serializable {

    private byte[] etc_pic;
    private String etcname;
    private String etcauth;
    private String etcwrite;


    public Activity_etc_data(String etcname, String etcauth, String etcwrite,byte[] etc_pic) {

        this.etc_pic = etc_pic;
        this.etcname = etcname;
        this.etcauth = etcauth;
        this.etcwrite = etcwrite;
        this.etc_pic = etc_pic;
    }


    public byte[] getEtc_pic() {

        return etc_pic;
    }

    public void setEtc_pic(byte[] etc_pic) {

        this.etc_pic = etc_pic;
    }

    public String getEtcname() {

        return etcname;
    }

    public void setEtcname(String etcname) {

        this.etcname = etcname;
    }

    public String getEtcauth() {

        return etcauth;
    }

    public void setEtcauth(String etcauth) {

        this.etcauth = etcauth;
    }

    public String getEtcwrite() {

        return etcwrite;
    }

    public void setEtcwrite(String etcwrite) {

        this.etcwrite = etcwrite;
    }
}


