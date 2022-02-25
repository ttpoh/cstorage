package com.nova.cstorage;

import java.io.Serializable;

public class Activity_memo_data implements Serializable {

    private String memoTitle;
    private String memoWrite;

    public Activity_memo_data(String memoTitle, String memoWrite) {

        this.memoTitle = memoTitle;
        this.memoWrite = memoWrite;
    }


    public String getMemoTitle() {

        return this.memoTitle;
    }

    public String setMemoTitle(String memoTitle) {

        return this.memoTitle;
    }

    public String getMemoWrite() {

        return this.memoWrite;
    }

    public String setMemoWrite(String memoWrite) {

        return this.memoWrite;

    }
}