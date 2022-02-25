package com.nova.cstorage.concert;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.MainActivity;
import com.nova.cstorage.R;
import com.nova.cstorage.concert.Activity_concert_add;
import com.nova.cstorage.concert.Activity_concert_adapter;
import com.nova.cstorage.concert.Activity_concert_data;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_concert_list extends AppCompatActivity implements View.OnClickListener, Activity_concert_adapter.OnListItemSelectedInterface {

    private ArrayList<Activity_concert_data> arrayConcert;
    private Activity_concert_adapter adapter;
    RecyclerView recyclerView;
    Gson gson = new GsonBuilder().create();
    String SharedPreFile = "cstorage_concertData";
    final Context context = this;
    String concertData = "concertData";
    Type arrayConcert_list = new TypeToken<ArrayList<Activity_concert_data>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concert_list);

        recyclerView = findViewById(R.id.concert_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        String concert_data = sharedPreferences.getString("concertData", "");
        if (concert_data.equals("")) {
            arrayConcert = new ArrayList<Activity_concert_data>();
            String concert_array = gson.toJson(arrayConcert, arrayConcert_list);
            editor.putString("concertData", concert_array);
            editor.commit();
        } else {

            arrayConcert = gson.fromJson(concert_data, arrayConcert_list);
        }

        Log.d(TAG, "arrayconcert : " + "사이즈" + arrayConcert.size());
        adapter = new Activity_concert_adapter(context, this, arrayConcert);
        recyclerView.setAdapter(adapter);

        Button Add_concert = (Button) findViewById(R.id.btn_addc_add);
        Button Add_concert_back = (Button) findViewById(R.id.btn_addc_back);

        Add_concert.setOnClickListener(this);
        Add_concert_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_addc_add) {
            Intent intent = new Intent(getApplicationContext(), Activity_concert_add.class);
            startActivity(intent);
            finish();

        } else if (v.getId() == R.id.btn_addc_back) {

            finish();
        }
    }


    @Override
    public void onItemSelected(View v, int position) {
        Activity_concert_adapter.CustomViewHolder viewHolder = (Activity_concert_adapter.CustomViewHolder) recyclerView.findViewHolderForAdapterPosition(position);


    }
}
