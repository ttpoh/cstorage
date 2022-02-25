package com.nova.cstorage.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.nova.cstorage.R;
import com.nova.cstorage.todolist.Activity_todolist_data;

import java.util.ArrayList;

public class todoAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Activity_todolist_data> tododata = new ArrayList<Activity_todolist_data>();
    private ViewHolder holder;


    public todoAdapter(Context mContext, int todolist_data, ArrayList<Activity_todolist_data> tododata) {
        this.mContext = mContext;
        this.tododata = tododata;
    }

    @Override
    public int getCount() {

        return tododata.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int pos) {
        return tododata.get(pos);
    }


private class ViewHolder {

    private TextView todolist;
}


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflater.inflate(R.layout.todolist_data, null);
            holder = new ViewHolder();
            holder.todolist = (TextView) convertView.findViewById(R.id.todo_item);

            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.todolist.setText(tododata.get(position).getTodo());

        return convertView;
}
}


