package com.nova.cstorage.movie;

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
import com.nova.cstorage.movie.Activity_add_movie;
import com.nova.cstorage.movie.Activity_movie_adapter;
import com.nova.cstorage.movie.Activity_movie_data;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_movie_list extends AppCompatActivity implements View.OnClickListener , Activity_movie_adapter.OnListItemSelectedInterface {

    private ArrayList<Activity_movie_data> arraymovie;
    private Activity_movie_adapter adapter;
    RecyclerView recyclerView;
    Gson gson = new GsonBuilder().create();
    String SharedPreFile = "cstorage_movieData";
    final Context context = this;
    String movieData = "movieData";
    Type arraymovie_list = new TypeToken<ArrayList<Activity_movie_data>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list);

        recyclerView = findViewById(R.id.movie_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        String movie_data = sharedPreferences.getString("movieData", "");
        if (movie_data.equals("")) {
            arraymovie = new ArrayList<Activity_movie_data>();
            String movie_array = gson.toJson(arraymovie, arraymovie_list);
            editor.putString("movieData", movie_array);
            editor.commit();
        } else {

            arraymovie = gson.fromJson(movie_data, arraymovie_list);
        }

        Log.d(TAG, "arraymovie : " + "사이즈" + arraymovie.size());
        adapter = new Activity_movie_adapter(context, this, arraymovie);
        recyclerView.setAdapter(adapter);

        Button Add_movie = (Button) findViewById(R.id.btn_b_add);
        Button Add_movie_back = (Button) findViewById(R.id.btn_b_back);

        Add_movie.setOnClickListener(this);
        Add_movie_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_b_add) {
            Intent intent = new Intent(getApplicationContext(), Activity_add_movie.class);
            startActivity(intent);
            finish();

        } else if (v.getId() == R.id.btn_b_back) {

            finish();
        }
    }


    @Override
    public void onItemSelected(View v, int position) {
        Activity_movie_adapter.CustomViewHolder viewHolder = (Activity_movie_adapter.CustomViewHolder) recyclerView.findViewHolderForAdapterPosition(position);


    }
}
