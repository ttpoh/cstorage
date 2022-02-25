package com.nova.cstorage.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;
import com.nova.cstorage.book.Activity_book_data;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_todolist_add extends AppCompatActivity implements View.OnClickListener {

    Button saveButton, cancleButton;
    EditText editTodo;
    Context context;
    todoAdapter adapter;
    ArrayList<Activity_todolist_data> todoItems;

    String todoData;
    String todoFile = "cstorage_todoData";
    SharedPreferences sharedPreferences;
    Gson gson = new Gson();

    Type arrayTodo = new TypeToken<ArrayList<Activity_todolist_data>>() {
    }.getType();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_add);

        saveButton = (Button) findViewById(R.id.btn_todod_s);
        cancleButton = (Button) findViewById(R.id.btn_todod_c);
        editTodo = (EditText) findViewById(R.id.todod_todo);


        sharedPreferences = getSharedPreferences(todoFile, MODE_PRIVATE);

        saveButton.setOnClickListener(this);
        cancleButton.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_todod_s:
                String todo = editTodo.getText().toString();

                if (todo.equals("")) {//빈값이 넘어올때의 처리
                    Toast myToast3 = Toast.makeText(this.getApplicationContext(), "내용을 입력해 주세요.", Toast.LENGTH_SHORT);
                    myToast3.show();
                } else {

                    Activity_todolist_data todolist_data = new Activity_todolist_data(todo);

                    todoData = sharedPreferences.getString("todoData", "");
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if (todoData.equals("")) {
                        Log.e("todo데이터추가:", "1" + todoData);
                        todoItems = new ArrayList<Activity_todolist_data>();
                        String addTodo = gson.toJson(todoItems, arrayTodo);
                        editor.putString("todoData", addTodo);
                        editor.commit();
                    } else {

                        todoItems = gson.fromJson(todoData, arrayTodo);
                    }
                    todoItems.add(todolist_data);
                    String addTodo = gson.toJson(todoItems, arrayTodo);
                    editor.putString("todoData", addTodo);
                    editor.commit();
                    Log.e("todo데이터추가:", "2" + todoData);




                Intent Intent_todoSave = new Intent(getApplicationContext(), Activity_todolist.class);
                startActivity(Intent_todoSave);

                finish();}
                break;
            case R.id.btn_todod_c:
                Intent Intent_todoCancle = new Intent(getApplicationContext(), Activity_todolist.class);
                startActivity(Intent_todoCancle);
                finish();
                break;
        }
    }
}
