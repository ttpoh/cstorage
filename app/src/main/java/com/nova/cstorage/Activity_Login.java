package com.nova.cstorage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Activity_Login extends AppCompatActivity implements View.OnClickListener {

    EditText inputMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        inputMail = (EditText) findViewById(R.id.login_email);

        Button btn_login = (Button) findViewById(R.id.btn_login);
        Button btn_join = (Button) findViewById(R.id.btn_join);
        Button btn_back = (Button) findViewById(R.id.btn_back);

        btn_login.setOnClickListener(this);
        btn_join.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            String mail = inputMail.getText().toString();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("1", mail);
            startActivity(intent);
        }
        if (v.getId() == R.id.btn_join) {
            Intent intent = new Intent(getApplicationContext(), Activity_Join.class);
            startActivity(intent);

        }
        if (v.getId() == R.id.btn_back) {
            Log.d("뒤로가기","1");
            Intent intent_back = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent_back);
            finish();
        }
    }

}
