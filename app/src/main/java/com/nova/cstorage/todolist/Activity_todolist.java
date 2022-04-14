package com.nova.cstorage.todolist;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.MainActivity;
import com.nova.cstorage.R;
import com.nova.cstorage.book.Activity_book_adapter;
import com.nova.cstorage.book.Activity_book_data;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_todolist extends AppCompatActivity implements View.OnClickListener {
    ListView listview;
    todoAdapter adapter;
    ArrayList<Activity_todolist_data> arrayTodo;
    final Context context = this;

    Gson gson = new Gson();
    String SharedPreFile = "cstorage_todoData";

    String todoData = "todoData";
    Type arrayTodolist = new TypeToken<ArrayList<Activity_todolist_data>>() {
    }.getType();


    final int NEW_TODO = 22;
    final int EDIT_TODO = 23;
    private Button btn_todos, btn_todoc;
    TextView todoadd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist);


        todoadd = (TextView) findViewById(R.id.todo_item);
        btn_todos = (Button) findViewById(R.id.btn_todo_add);
        btn_todoc = (Button) findViewById(R.id.btn_todo_cancle);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        String todoData = sharedPreferences.getString("todoData", "");
        if (todoData.equals("")) {
            arrayTodo = new ArrayList<Activity_todolist_data>();
            String book_array = gson.toJson(arrayTodo, arrayTodolist);
            editor.putString("todoData", book_array);
            editor.commit();
        } else {

            arrayTodo = gson.fromJson(todoData, arrayTodolist);
        }

        Log.d(TAG, "arraybook : " + "사이즈" + arrayTodo.size());

        adapter = new todoAdapter(context, R.layout.todolist_data, arrayTodo);
        adapter.notifyDataSetChanged();

        listview = (ListView) findViewById(R.id.todolistview);
        listview.setAdapter(adapter);

        btn_todos.setOnClickListener(this);
        btn_todoc.setOnClickListener(this);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {


                Intent Intent_todoE = new Intent(getApplicationContext(), Activity_todolist_edit.class);
                Intent_todoE.putExtra("pos", pos);
                startActivity(Intent_todoE);

            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                String st[] = {"1.예", "2.아니오"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("삭제하시겠습니까?");
                builder.setItems(st, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        if (i == 0) {
                            int pos;
                            pos = position;
                            Log.d(TAG, "arraybook : " + pos);
                            SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreFile, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            String todoDataD = sharedPreferences.getString("todoData", "");

                            arrayTodo = gson.fromJson(todoDataD, arrayTodolist);
                            arrayTodo.remove(pos);
                            adapter = new todoAdapter(context, R.layout.todolist_data, arrayTodo);

                            String dTodo = gson.toJson(arrayTodo, arrayTodolist);
                            editor.putString("todoData", dTodo);
                            editor.commit();


                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
//                            Toast.makeText(context, "삭제 버튼", Toast.LENGTH_SHORT).show();


                        } else if (i == 1) {
//                            Toast.makeText(context, "아니오", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_todo_add:
                Intent add_todo = new Intent(getApplicationContext(), Activity_todolist_add.class);
                startActivity(add_todo);
                finish();
                break;
            case R.id.btn_todo_cancle:
//                Intent can_todo = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(can_todo);
                finish();
                break;
        }
    }
}

