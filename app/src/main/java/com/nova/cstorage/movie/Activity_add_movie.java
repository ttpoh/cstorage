package com.nova.cstorage.movie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;
import com.nova.cstorage.movie.Activity_movie_data;
import com.nova.cstorage.movie.Activity_movie_list;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class Activity_add_movie extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 0;
    EditText input_moviedirector;
    EditText input_movietitle;
    EditText input_moviereview;

    Bitmap sendBitmap;
    Bitmap img;
    private ImageView imageView;
    ArrayList<Activity_movie_data> movie_item;

    String movieData;
    String SharedPreFile = "cstorage_movieData";
    SharedPreferences sharedPreferences;
    Gson gson;
    Context context;

    Type arraymovie_list = new TypeToken<ArrayList<Activity_movie_data>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_add);

        imageView = findViewById(R.id.btn_addm_img);

        input_movietitle = (EditText) findViewById(R.id.movie_add_title);
        input_moviedirector = (EditText) findViewById(R.id.movie_add_director);
        input_moviereview = (EditText) findViewById(R.id.movie_add_review);

        Button btn_addm_save = (Button) findViewById(R.id.btn_addm_save);
        Button btn_addm_cancle = (Button) findViewById(R.id.btn_addm_cancle);
        ImageButton btn_addm_img = (ImageButton) findViewById(R.id.btn_addm_img);


        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);

        btn_addm_save.setOnClickListener(this);
        btn_addm_cancle.setOnClickListener(this);
        btn_addm_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_addm_save) {

            String movie_title = input_movietitle.getText().toString();
            String movie_author = input_moviedirector.getText().toString();
            String movie_review = input_moviereview.getText().toString();


            if (movie_review.equals("") && movie_author.equals("") && movie_title.equals("")) {//빈값이 넘어올때의 처리
                Toast myToast3 = Toast.makeText(this.getApplicationContext(), "내용을 입력해 주세요.", Toast.LENGTH_SHORT);
                myToast3.show();
            } else {


                sendBitmap = BitmapFactory.decodeResource(getResources(), R.id.btn_addm_img);
                sendBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                float scale = (float) (1024 / (float) sendBitmap.getWidth());
                int image_w = (int) (sendBitmap.getWidth() * scale);
                int image_h = (int) (sendBitmap.getHeight() * scale);
                Bitmap resize = Bitmap.createScaledBitmap(sendBitmap, image_w, image_h, true);
                resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                gson = new Gson();
                Activity_movie_data activity_movie_data = new Activity_movie_data(movie_title, movie_author, movie_review, byteArray);

                String movie_data = sharedPreferences.getString("movieData", "");

                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (movie_data.equals("")) {

                    movie_item = new ArrayList<Activity_movie_data>();
                    String addmovie = gson.toJson(movie_item, arraymovie_list);
                    editor.putString("movieData", addmovie);
                    editor.commit();
                } else {

                    movie_item = gson.fromJson(movie_data, arraymovie_list);
                }

                movie_item.add(activity_movie_data);
                String arraylist = gson.toJson(movie_item, arraymovie_list);
                editor.putString("movieData", arraylist);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Activity_movie_list.class);
                startActivity(intent);


                finish();
            }
        }
        if (v.getId() == R.id.btn_addm_cancle) {
            Intent intent = new Intent(getApplicationContext(), Activity_movie_list.class);
            startActivity(intent);
            finish();
        }
        if (v.getId() == R.id.btn_addm_img) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            Toast myToast2 = Toast.makeText(this.getApplicationContext(), "이미지 저장버튼", Toast.LENGTH_SHORT);
            myToast2.show();
            startActivityForResult(intent, REQUEST_CODE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    img = BitmapFactory.decodeStream(in);
                    in.close();

                    imageView.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
}





