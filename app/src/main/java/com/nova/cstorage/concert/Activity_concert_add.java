package com.nova.cstorage.concert;

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
import com.nova.cstorage.concert.Activity_concert_data;
import com.nova.cstorage.concert.Activity_concert_list;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_concert_add extends AppCompatActivity implements View.OnClickListener {



    EditText input_concertPerformer;
    EditText input_concertTitle;
    EditText input_concertReview;

    private static final int REQUEST_CODE = 0;
    Bitmap sendBitmap;
    Bitmap img;
    private ImageView imageView;
    ArrayList<Activity_concert_data> concert_item;

    String concertData;
    String SharedPreFile = "cstorage_concertData";
    SharedPreferences sharedPreferences;
    Gson gson;
    Context context;

    Type arrayConcert_list = new TypeToken<ArrayList<Activity_concert_data>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concert_add);

        imageView = findViewById(R.id.btn_addc_img);

        input_concertTitle = (EditText) findViewById(R.id.con_add_title);
        input_concertPerformer = (EditText) findViewById(R.id.con_add_performer);
        input_concertReview = (EditText) findViewById(R.id.con_add_review);

        Button btn_addc_save = (Button) findViewById(R.id.btn_addc_save);
        Button btn_addc_cancle = (Button) findViewById(R.id.btn_addc_cancle);
        ImageButton btn_addc_img = (ImageButton) findViewById(R.id.btn_addc_img);


        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);

        btn_addc_save.setOnClickListener(this);
        btn_addc_cancle.setOnClickListener(this);
        btn_addc_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_addc_save) {

            String concert_title = input_concertTitle.getText().toString();
            String concert_author = input_concertPerformer.getText().toString();
            String concert_review = input_concertReview.getText().toString();


            if (concert_review.equals("") && concert_author.equals("") && concert_title.equals("")) {//빈값이 넘어올때의 처리
                Toast myToast3 = Toast.makeText(this.getApplicationContext(), "내용을 입력해 주세요.", Toast.LENGTH_SHORT);
                myToast3.show();
            } else {


                sendBitmap = BitmapFactory.decodeResource(getResources(), R.id.btn_addc_img);
                sendBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                float scale = (float) (1024 / (float) sendBitmap.getWidth());
                int image_w = (int) (sendBitmap.getWidth() * scale);
                int image_h = (int) (sendBitmap.getHeight() * scale);
                Bitmap resize = Bitmap.createScaledBitmap(sendBitmap, image_w, image_h, true);
                resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                gson = new Gson();
                Activity_concert_data activity_concert_data = new Activity_concert_data(concert_title, concert_author, concert_review, byteArray);

                String concert_data = sharedPreferences.getString("concertData", "");

                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (concert_data.equals("")) {

                    concert_item = new ArrayList<Activity_concert_data>();
                    String addconcert = gson.toJson(concert_item, arrayConcert_list);
                    editor.putString("concertData", addconcert);
                    editor.commit();
                } else {

                    concert_item = gson.fromJson(concert_data, arrayConcert_list);
                }

                concert_item.add(activity_concert_data);
                String arraylist = gson.toJson(concert_item, arrayConcert_list);
                editor.putString("concertData", arraylist);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Activity_concert_list.class);
                startActivity(intent);


                finish();
            }
        }
        if (v.getId() == R.id.btn_addc_cancle) {
            Intent intent = new Intent(getApplicationContext(), Activity_concert_list.class);
            startActivity(intent);
            finish();
        }
        if (v.getId() == R.id.btn_addc_img) {
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





