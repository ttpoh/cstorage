package com.nova.cstorage.etc;


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
import com.nova.cstorage.etc.Activity_etc_data;
import com.nova.cstorage.etc.Activity_etc_list;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_etc_add extends AppCompatActivity implements View.OnClickListener {



    EditText input_etcDate;
    EditText input_etcSite;
    EditText input_etcReview;

    private static final int REQUEST_CODE = 0;
    Bitmap sendBitmap;
    Bitmap img;
    private ImageView imageView;
    ArrayList<Activity_etc_data> etc_item;

    String etcData;
    String SharedPreFile = "cstorage_etcData";
    SharedPreferences sharedPreferences;
    Gson gson;
    Context context;

    Type arrayEtc_list = new TypeToken<ArrayList<Activity_etc_data>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etc_add);

        imageView = findViewById(R.id.btn_adde_img);

        input_etcSite = (EditText) findViewById(R.id.etc_add_title);
        input_etcDate = (EditText) findViewById(R.id.etc_add_date);
        input_etcReview = (EditText) findViewById(R.id.etc_add_review);

        Button btn_adde_save = (Button) findViewById(R.id.btn_adde_save);
        Button btn_adde_cancle = (Button) findViewById(R.id.btn_adde_cancle);
        ImageButton btn_adde_img = (ImageButton) findViewById(R.id.btn_adde_img);


        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);

        btn_adde_save.setOnClickListener(this);
        btn_adde_cancle.setOnClickListener(this);
        btn_adde_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_adde_save) {

            String etc_site = input_etcSite.getText().toString();
            String etc_date = input_etcDate.getText().toString();
            String etc_review = input_etcReview.getText().toString();


            if (etc_review.equals("") && etc_date.equals("") && etc_site.equals("")) {//빈값이 넘어올때의 처리
                Toast myToast3 = Toast.makeText(this.getApplicationContext(), "내용을 입력해 주세요.", Toast.LENGTH_SHORT);
                myToast3.show();
            } else {


                sendBitmap = BitmapFactory.decodeResource(getResources(), R.id.btn_adde_img);
                sendBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                float scale = (float) (1024 / (float) sendBitmap.getWidth());
                int image_w = (int) (sendBitmap.getWidth() * scale);
                int image_h = (int) (sendBitmap.getHeight() * scale);
                Bitmap resize = Bitmap.createScaledBitmap(sendBitmap, image_w, image_h, true);
                resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                gson = new Gson();
                Activity_etc_data activity_etc_data = new Activity_etc_data(etc_site, etc_date, etc_review, byteArray);

                String etc_data = sharedPreferences.getString("etcData", "");

                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (etc_data.equals("")) {

                    etc_item = new ArrayList<Activity_etc_data>();
                    String addetc = gson.toJson(etc_item, arrayEtc_list);
                    editor.putString("etcData", addetc);
                    editor.commit();
                } else {

                    etc_item = gson.fromJson(etc_data, arrayEtc_list);
                }

                etc_item.add(activity_etc_data);
                String arraylist = gson.toJson(etc_item, arrayEtc_list);
                editor.putString("etcData", arraylist);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Activity_etc_list.class);
                startActivity(intent);


                finish();
            }
        }
        if (v.getId() == R.id.btn_adde_cancle) {
            Intent intent = new Intent(getApplicationContext(), Activity_etc_list.class);
            startActivity(intent);
            finish();
        }
        if (v.getId() == R.id.btn_adde_img) {
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






