package com.nova.cstorage;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_Join extends AppCompatActivity implements View.OnClickListener {

    EditText inputMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
    }

    @Override
    public void onClick(View v) {

    }
}