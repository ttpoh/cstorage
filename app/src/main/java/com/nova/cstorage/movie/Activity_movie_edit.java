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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;
import com.nova.cstorage.movie.Activity_movie_adapter;
import com.nova.cstorage.movie.Activity_movie_data;
import com.nova.cstorage.movie.Activity_movie_list;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_movie_edit extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 0;

    Button btn_mes;
    Button btn_mec;
    EditText editMet;
    EditText editMea;
    EditText editMer;
    Gson gson;
    String SharedPreFile = "cstorage_movieData";
    Bitmap sendBitmap, image;
    private ImageView imageView;
    SharedPreferences sharedPreferences;
    private Intent intent;
    Context context;
    ArrayList<Activity_movie_data> edit_arraymovie;
    private Activity_movie_adapter adapter;

    Type arraymovie_list = new TypeToken<ArrayList<Activity_movie_data>>() {
    }.getType();
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_edit);

        intent = getIntent();
        editMet = (EditText) findViewById(R.id.movie_edit_title);
        editMea = (EditText) findViewById(R.id.movie_edit_author);
        editMer = (EditText) findViewById(R.id.movie_edit_review);
        imageView = findViewById(R.id.btn_editm_img);


        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        gson = new Gson();

        String movie_data = sharedPreferences.getString("movieData", "");
        if (movie_data.equals("")) {
            edit_arraymovie = new ArrayList<Activity_movie_data>();
            String movie_array = gson.toJson(edit_arraymovie, arraymovie_list);
            editor.putString("movieData", movie_array);
            editor.commit();
        } else {

            edit_arraymovie = gson.fromJson(movie_data, arraymovie_list);
        }


        pos = intent.getIntExtra("position1", -1);

        byte[] arr = edit_arraymovie.get(pos).getMovie_pic();
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        imageView.setImageBitmap(image);
        editMet.setText(edit_arraymovie.get(pos).getMoviename());
        editMea.setText(edit_arraymovie.get(pos).getMovieauth());
        editMer.setText(edit_arraymovie.get(pos).getMoviewrite());

//
//
//        Toast myToast2 = Toast.makeText(this.getApplicationContext(), "수정 데이터 이동", Toast.LENGTH_SHORT);
//        myToast2.show();
//
//        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);

        btn_mes = findViewById(R.id.btn_editm_edit);
        btn_mec = findViewById(R.id.btn_editm_cancle);
        ImageButton btn_editm_img = (ImageButton) findViewById(R.id.btn_editm_img);

        btn_editm_img.setOnClickListener(this);
        btn_mes.setOnClickListener(this);
        btn_mec.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        Toast myToast = Toast.makeText(this.getApplicationContext(), "추가버튼", Toast.LENGTH_SHORT);
//        myToast.show();

        switch (v.getId()) {
            case R.id.btn_editm_edit:
                String ebTitle = editMet.getText().toString();
                String ebAuthor = editMea.getText().toString();
                String ebWrite = editMer.getText().toString();
//                Toast myToast1 = Toast.makeText(this.getApplicationContext(), "수정버튼", Toast.LENGTH_SHORT);
//                myToast1.show();

                sendBitmap = BitmapFactory.decodeResource(getResources(), R.id.btn_addb_img);
                sendBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                float scale = (float) (1024 / (float) sendBitmap.getWidth());
                int image_w = (int) (sendBitmap.getWidth() * scale);
                int image_h = (int) (sendBitmap.getHeight() * scale);
                Bitmap resize = Bitmap.createScaledBitmap(sendBitmap, image_w, image_h, true);
                resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                gson = new GsonBuilder().create();
                Activity_movie_data activity_movie_data = new Activity_movie_data(ebTitle, ebAuthor, ebWrite, byteArray);
//                String edit_bdata = gson.toJson(activity_movie_data, Activity_movie_data.class);


                String movie_data = sharedPreferences.getString("movieData", "");

                if (movie_data.equals("")) {

                    edit_arraymovie = new ArrayList<Activity_movie_data>();
                    String movie_array = gson.toJson(edit_arraymovie, arraymovie_list);
                    editor.putString("movieData", movie_array);
                    editor.commit();
                } else {

                    edit_arraymovie = gson.fromJson(movie_data, arraymovie_list);
                }

                edit_arraymovie.set(pos, activity_movie_data);
                String arraylist = gson.toJson(edit_arraymovie, arraymovie_list);
                editor.putString("movieData", arraylist);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Activity_movie_list.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_editm_cancle:
                Intent intent1 = new Intent(getApplicationContext(), Activity_movie_list.class);
                startActivity(intent1);
//                Toast myToast2 = Toast.makeText(this.getApplicationContext(), "수정취소", Toast.LENGTH_SHORT);
//                myToast2.show();
                finish();

                break;
            case R.id.btn_editm_img:
                Intent intent2 = new Intent();
                intent2.setType("image/*");
                intent2.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent2, REQUEST_CODE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
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


