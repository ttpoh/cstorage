package com.nova.cstorage.etc;

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
import com.nova.cstorage.etc.Activity_etc_add;
import com.nova.cstorage.etc.Activity_etc_adapter;
import com.nova.cstorage.etc.Activity_etc_data;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_etc_list extends AppCompatActivity implements View.OnClickListener, Activity_etc_adapter.OnListItemSelectedInterface {

    private ArrayList<Activity_etc_data> arrayEtc;
    private Activity_etc_adapter adapter;
    RecyclerView recyclerView;
    Gson gson = new GsonBuilder().create();
    String SharedPreFile = "cstorage_etcData";
    final Context context = this;
    String etcData = "etcData";
    Type arrayEtc_list = new TypeToken<ArrayList<Activity_etc_data>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etc_list);

        recyclerView = findViewById(R.id.etc_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        String etc_data = sharedPreferences.getString("etcData", "");
        if (etc_data.equals("")) {
            arrayEtc = new ArrayList<Activity_etc_data>();
            String etc_array = gson.toJson(arrayEtc, arrayEtc_list);
            editor.putString("etcData", etc_array);
            editor.commit();
        } else {

            arrayEtc = gson.fromJson(etc_data, arrayEtc_list);
        }

        Log.d(TAG, "arrayetc : " + "사이즈" + arrayEtc.size());
        adapter = new Activity_etc_adapter(context, this, arrayEtc);
        recyclerView.setAdapter(adapter);

        Button Add_etc = (Button) findViewById(R.id.btn_etc_add);
        Button Add_etc_back = (Button) findViewById(R.id.btn_etc_back);

        Add_etc.setOnClickListener(this);
        Add_etc_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_etc_add) {
            Intent intent = new Intent(getApplicationContext(), Activity_etc_add.class);
            startActivity(intent);
            finish();

        } else if (v.getId() == R.id.btn_etc_back) {

            finish();
        }
    }


    @Override
    public void onItemSelected(View v, int position) {
        Activity_etc_adapter.CustomViewHolder viewHolder = (Activity_etc_adapter.CustomViewHolder) recyclerView.findViewHolderForAdapterPosition(position);


    }
}
