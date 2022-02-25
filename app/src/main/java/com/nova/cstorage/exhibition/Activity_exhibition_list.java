package com.nova.cstorage.exhibition;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.MainActivity;
import com.nova.cstorage.R;
import com.nova.cstorage.exhibition.Activity_add_exhibition;
import com.nova.cstorage.exhibition.Activity_exhibition_adapter;
import com.nova.cstorage.exhibition.Activity_exhibition_data;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_exhibition_list extends AppCompatActivity implements View.OnClickListener, Activity_exhibition_adapter.OnListItemSelectedInterface {

    private ArrayList<Activity_exhibition_data> arrayExhibition;
    private Activity_exhibition_adapter adapter;
    RecyclerView recyclerView;
    Gson gson = new GsonBuilder().create();
    String SharedPreFile = "cstorage_exhibitionData";
    final Context context = this;

    Type arrayExhibition_list = new TypeToken<ArrayList<Activity_exhibition_data>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exhibition_list);

        recyclerView = findViewById(R.id.exhibition_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        String exhibition_data = sharedPreferences.getString("exhibitionData", "");
        if (exhibition_data.equals("")) {
            arrayExhibition = new ArrayList<Activity_exhibition_data>();
            String exhibition_array = gson.toJson(arrayExhibition, arrayExhibition_list);
            editor.putString("exhibitionData", exhibition_array);
            editor.commit();
        } else {

            arrayExhibition = gson.fromJson(exhibition_data, arrayExhibition_list);
        }


        adapter = new Activity_exhibition_adapter(context, this, arrayExhibition);
        recyclerView.setAdapter(adapter);

        Button Add_exhibition = (Button) findViewById(R.id.btn_EX_add);
        Button Add_exhibition_back = (Button) findViewById(R.id.btn_EX_back);

        Add_exhibition.setOnClickListener(this);
        Add_exhibition_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_EX_add) {
            Intent intent = new Intent(getApplicationContext(), Activity_add_exhibition.class);
            startActivity(intent);
            finish();

        } else if (v.getId() == R.id.btn_EX_back) {

            finish();
        }
    }


    @Override
    public void onItemSelected(View v, int position) {
        Activity_exhibition_adapter.CustomViewHolder viewHolder = (Activity_exhibition_adapter.CustomViewHolder) recyclerView.findViewHolderForAdapterPosition(position);


    }
}
