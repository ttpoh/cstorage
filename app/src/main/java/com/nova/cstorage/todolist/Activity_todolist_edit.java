package com.nova.cstorage.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;
import com.nova.cstorage.book.Activity_book_data;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_todolist_edit extends AppCompatActivity implements View.OnClickListener {
    EditText editTodo;

    String etodo;

    ArrayList<Activity_todolist_data> edit_arrayTodo;
    todoAdapter adapter;
    SharedPreferences sharedPreferences;
    String todoFile = "cstorage_todoData";
    Gson gson = new GsonBuilder().create();

    Type arrayTodoE = new TypeToken<ArrayList<Activity_todolist_data>>() {
    }.getType();

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_edit);

        Intent intent = getIntent();

        editTodo = (EditText) findViewById(R.id.todo_edit_todo);

        sharedPreferences = getSharedPreferences(todoFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        gson = new Gson();

        String todo_data = sharedPreferences.getString("todoData", "");
        if (todo_data.equals("")) {
            edit_arrayTodo = new ArrayList<Activity_todolist_data>();
            String todo_array = gson.toJson(edit_arrayTodo, arrayTodoE);
            editor.putString("todoData", todo_array);
            editor.commit();
        } else {

            edit_arrayTodo = gson.fromJson(todo_data, arrayTodoE);
        }


        pos = intent.getIntExtra("pos", -1);
        adapter = new todoAdapter(this, R.layout.todolist_data, edit_arrayTodo);
        editTodo.setText(edit_arrayTodo.get(pos).getTodo());


        Button EsaveButton = (Button) findViewById(R.id.btn_todo_esave);
        Button EcancleButton = (Button) findViewById(R.id.btn_todo_ecancle);

        EsaveButton.setOnClickListener(this);
        EcancleButton.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_todo_esave:
                etodo = editTodo.getText().toString();


                Activity_todolist_data todolist_data = new Activity_todolist_data(etodo);

                String todoDataE = sharedPreferences.getString("todoData", "");
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (todoDataE.equals("")) {

                    edit_arrayTodo = new ArrayList<Activity_todolist_data>();
                    String addTodo = gson.toJson(edit_arrayTodo, arrayTodoE);
                    editor.putString("todoData", addTodo);
                    editor.commit();
                } else {

                    edit_arrayTodo = gson.fromJson(todoDataE, arrayTodoE);
                }
                edit_arrayTodo.set(pos, todolist_data);
                String addTodo = gson.toJson(edit_arrayTodo, arrayTodoE);
                editor.putString("todoData", addTodo);
                editor.commit();

                Intent Intent_todoE = new Intent(getApplicationContext(), Activity_todolist.class);
                startActivity(Intent_todoE);

                finish();
                break;
            case R.id.btn_todo_ecancle:
                finish();
                break;
        }
    }
}
