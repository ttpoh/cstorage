package com.nova.cstorage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.Activity_memo_data;
import com.nova.cstorage.todolist.Activity_todolist_data;
import com.nova.cstorage.todolist.todoAdapter;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_memo extends AppCompatActivity implements View.OnClickListener {
    EditText input_memotitle;
    EditText input_memowrite;
    ArrayList<Activity_memo_data> memo_item;
    String SharedPreFile = "cstorage_memoData";
    SharedPreferences sharedPreferences;
    Activity_memo_data activity_memo_data;
    String memo_title;
    String memo_write;
    Gson gson;
    Type memoItem = new TypeToken<Activity_memo_data>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo);


        input_memotitle = (EditText) findViewById(R.id.memo_title);
        input_memowrite = (EditText) findViewById(R.id.memo_write);
        Button btn_memo_save = (Button) findViewById(R.id.btn_memo_save);
        Button btn_memo_cancle = (Button) findViewById(R.id.btn_memo_cancle);

        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String memo_data = sharedPreferences.getString("memoData", "");


        if (memo_data.equals("")) {
            memo_title = input_memotitle.getText().toString();
            memo_write = input_memowrite.getText().toString();
            activity_memo_data = new Activity_memo_data(memo_title,memo_write);
            activity_memo_data = gson.fromJson(memo_data, memoItem);
            input_memotitle.setText(activity_memo_data.getMemoTitle());


        }else{gson = new Gson();



            input_memotitle.setText(activity_memo_data.getMemoTitle());
            input_memowrite.setText(activity_memo_data.getMemoWrite());
        }

        btn_memo_save.setOnClickListener(this);
        btn_memo_cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_memo_save) {

            memo_title = input_memotitle.getText().toString();
            memo_write = input_memowrite.getText().toString();


            if (memo_title.equals("") && memo_write.equals("")) {//빈값이 넘어올때의 처리
                Toast myToast3 = Toast.makeText(this.getApplicationContext(), "내용을 입력해 주세요.", Toast.LENGTH_SHORT);
                myToast3.show();
            } else {


                gson = new Gson();
                Activity_memo_data activity_memo_data = new Activity_memo_data(memo_title, memo_write);

                String memo_data = sharedPreferences.getString("memoData", "");

                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (memo_data.equals("")) {

//                    memo_item = new ArrayList<Activity_memo_data>();
                    String addMemo = gson.toJson(activity_memo_data);
                    editor.putString("memmoData", addMemo);
                    editor.commit();

                }
            }
        }
            if (v.getId() == R.id.btn_memo_cancle) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();


            }
        }
    }

