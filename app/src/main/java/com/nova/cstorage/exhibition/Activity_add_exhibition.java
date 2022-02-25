package com.nova.cstorage.exhibition;

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
import com.nova.cstorage.exhibition.Activity_exhibition_data;
import com.nova.cstorage.exhibition.Activity_exhibition_list;
import com.nova.cstorage.movie.Activity_movie_list;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class Activity_add_exhibition extends AppCompatActivity implements View.OnClickListener {

    EditText input_exhibition_artist;
    EditText input_exhibition_title;
    EditText input_exhibition_review;

    private static final int REQUEST_CODE = 0;
    Bitmap sendBitmap;
    Bitmap img;
    private ImageView imageView;
    ArrayList<Activity_exhibition_data> exhibition_item;

    String exhibitionData;
    String SharedPreFile = "cstorage_exhibitionData";
    SharedPreferences sharedPreferences;
    Gson gson;
    Context context;

    Type arrayexhibition_list = new TypeToken<ArrayList<Activity_exhibition_data>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exhibition_add);

        imageView = findViewById(R.id.btn_addex_img);

        input_exhibition_title = (EditText) findViewById(R.id.ex_add_title);
        input_exhibition_artist = (EditText) findViewById(R.id.ex_add_artist);
        input_exhibition_review = (EditText) findViewById(R.id.ex_add_review);

        Button btn_addex_save = (Button) findViewById(R.id.btn_addex_save);
        Button btn_addex_cancle = (Button) findViewById(R.id.btn_addex_cancle);
        ImageButton btn_addex_img = (ImageButton) findViewById(R.id.btn_addex_img);


        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);

        btn_addex_save.setOnClickListener(this);
        btn_addex_cancle.setOnClickListener(this);
        btn_addex_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_addex_save) {

            String exhibition_title = input_exhibition_title.getText().toString();
            String exhibition_author = input_exhibition_artist.getText().toString();
            String exhibition_review = input_exhibition_review.getText().toString();


            if (exhibition_review.equals("") && exhibition_author.equals("") && exhibition_title.equals("")) {//빈값이 넘어올때의 처리
                Toast myToast3 = Toast.makeText(this.getApplicationContext(), "내용을 입력해 주세요.", Toast.LENGTH_SHORT);
                myToast3.show();
            } else {


                sendBitmap = BitmapFactory.decodeResource(getResources(), R.id.btn_addex_img);
                sendBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                float scale = (float) (1024 / (float) sendBitmap.getWidth());
                int image_w = (int) (sendBitmap.getWidth() * scale);
                int image_h = (int) (sendBitmap.getHeight() * scale);
                Bitmap resize = Bitmap.createScaledBitmap(sendBitmap, image_w, image_h, true);
                resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                gson = new Gson();
                Activity_exhibition_data activity_exhibition_data = new Activity_exhibition_data(exhibition_title, exhibition_author, exhibition_review, byteArray);

                String exhibition_data = sharedPreferences.getString("exhibitionData", "");

                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (exhibition_data.equals("")) {

                    exhibition_item = new ArrayList<Activity_exhibition_data>();
                    String addexhibition = gson.toJson(exhibition_item, arrayexhibition_list);
                    editor.putString("exhibitionData", addexhibition);
                    editor.commit();
                } else {

                    exhibition_item = gson.fromJson(exhibition_data, arrayexhibition_list);
                }

                exhibition_item.add(activity_exhibition_data);
                String arraylist = gson.toJson(exhibition_item, arrayexhibition_list);
                editor.putString("exhibitionData", arraylist);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Activity_exhibition_list.class);
                startActivity(intent);


                finish();
            }
        }
        if (v.getId() == R.id.btn_addex_cancle) {
            Intent intent = new Intent(getApplicationContext(), Activity_exhibition_list.class);
            startActivity(intent);
            finish();
        }
        if (v.getId() == R.id.btn_addex_img) {
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





