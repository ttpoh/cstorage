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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;
import com.nova.cstorage.concert.Activity_concert_adapter;
import com.nova.cstorage.concert.Activity_concert_data;
import com.nova.cstorage.concert.Activity_concert_list;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_concert_edit extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 0;

    Button btn_Ces;
    Button btn_Cec;
    EditText editCet;
    EditText editCea;
    EditText editCer;
    Gson gson;
    String SharedPreFile = "cstorage_concertData";
    Bitmap sendBitmap, image;
    private ImageView imageView;
    SharedPreferences sharedPreferences;
    private Intent intent;
    Context context;
    ArrayList<Activity_concert_data> edit_arrayConcert;
    private Activity_concert_adapter adapter;

    Type arrayConcert_list = new TypeToken<ArrayList<Activity_concert_data>>() {
    }.getType();
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concert_edit);

        intent = getIntent();
        editCet = (EditText) findViewById(R.id.concert_edit_title);
        editCea = (EditText) findViewById(R.id.concert_edit_author);
        editCer = (EditText) findViewById(R.id.concert_edit_review);
        imageView = findViewById(R.id.btn_editc_img);


        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        gson = new Gson();

        String concert_data = sharedPreferences.getString("concertData", "");
        if (concert_data.equals("")) {
            edit_arrayConcert = new ArrayList<Activity_concert_data>();
            String concert_array = gson.toJson(edit_arrayConcert, arrayConcert_list);
            editor.putString("concertData", concert_array);
            editor.commit();
        } else {

            edit_arrayConcert = gson.fromJson(concert_data, arrayConcert_list);
        }


        pos = intent.getIntExtra("position1", -1);

        byte[] arr = edit_arrayConcert.get(pos).getConcert_pic();
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        imageView.setImageBitmap(image);
        editCet.setText(edit_arrayConcert.get(pos).getConcertName());
        editCea.setText(edit_arrayConcert.get(pos).getConcertAuth());
        editCer.setText(edit_arrayConcert.get(pos).getConcertWrite());

//
//
//        Toast myToast2 = Toast.makeText(this.getApplicationContext(), "수정 데이터 이동", Toast.LENGTH_SHORT);
//        myToast2.show();
//
//        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);

        btn_Ces = findViewById(R.id.btn_editc_edit);
        btn_Cec = findViewById(R.id.btn_editc_cancle);
        ImageButton btn_editb_img = (ImageButton) findViewById(R.id.btn_editc_img);

        btn_editb_img.setOnClickListener(this);
        btn_Ces.setOnClickListener(this);
        btn_Cec.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast myToast = Toast.makeText(this.getApplicationContext(), "추가버튼", Toast.LENGTH_SHORT);
        myToast.show();

        switch (v.getId()) {
            case R.id.btn_editc_edit:
                String ecTitle = editCet.getText().toString();
                String ecAuthor = editCea.getText().toString();
                String ecWrite = editCer.getText().toString();
                Toast myToast1 = Toast.makeText(this.getApplicationContext(), "수정버튼", Toast.LENGTH_SHORT);
                myToast1.show();

                sendBitmap = BitmapFactory.decodeResource(getResources(), R.id.btn_addc_img);
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
                Activity_concert_data activity_concert_data = new Activity_concert_data(ecTitle, ecAuthor, ecWrite, byteArray);
//                String edit_bdata = gson.toJson(activity_concert_data, Activity_concert_data.class);


                String concert_data = sharedPreferences.getString("concertData", "");

                if (concert_data.equals("")) {

                    edit_arrayConcert = new ArrayList<Activity_concert_data>();
                    String concert_array = gson.toJson(edit_arrayConcert, arrayConcert_list);
                    editor.putString("concertData", concert_array);
                    editor.commit();
                } else {

                    edit_arrayConcert = gson.fromJson(concert_data, arrayConcert_list);
                }

                edit_arrayConcert.set(pos, activity_concert_data);
                String arraylist = gson.toJson(edit_arrayConcert, arrayConcert_list);
                editor.putString("concertData", arraylist);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Activity_concert_list.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_editc_cancle:
                Intent intent1 = new Intent(getApplicationContext(), Activity_concert_list.class);
                startActivity(intent1);
                Toast myToast2 = Toast.makeText(this.getApplicationContext(), "수정취소", Toast.LENGTH_SHORT);
                myToast2.show();
                finish();

                break;
            case R.id.btn_editc_img:
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


