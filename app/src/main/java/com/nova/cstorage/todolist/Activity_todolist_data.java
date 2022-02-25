package com.nova.cstorage.todolist;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Activity_todolist_data implements Serializable {

    private String todo;

    public Activity_todolist_data(String todo) {
        this.todo = todo;
    }


    public String getTodo() {

        return this.todo;
    }

    public String setTodo(String todo) {

        return this.todo;
    }


}
