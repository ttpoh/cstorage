package com.nova.cstorage.todolist;

import android.view.View;
import android.widget.TextView;

import com.nova.cstorage.R;

public class ViewHolder {
    TextView addTodo;

    public ViewHolder(View convertView) {

        addTodo = (TextView) convertView.findViewById(R.id.todo_item);

    }
}
