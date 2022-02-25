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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;
import com.nova.cstorage.etc.Activity_etc_adapter;
import com.nova.cstorage.etc.Activity_etc_data;
import com.nova.cstorage.etc.Activity_etc_list;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_etc_edit extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 0;

    Button btn_ees;
    Button btn_eec;
    EditText editEet;
    EditText editEea;
    EditText editEer;
    Gson gson;
    String SharedPreFile = "cstorage_etcData";
    Bitmap sendBitmap, image;
    private ImageView imageView;
    SharedPreferences sharedPreferences;
    private Intent intent;
    Context context;
    ArrayList<Activity_etc_data> edit_arrayEtc;
    private Activity_etc_adapter adapter;

    Type arrayEtc_list = new TypeToken<ArrayList<Activity_etc_data>>() {
    }.getType();
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.etc_edit);

        intent = getIntent();
        editEet = (EditText) findViewById(R.id.etc_edit_title);
        editEea = (EditText) findViewById(R.id.etc_edit_author);
        editEer = (EditText) findViewById(R.id.etc_edit_review);
        imageView = findViewById(R.id.btn_edite_img);


        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        gson = new Gson();

        String etc_data = sharedPreferences.getString("etcData", "");
        if (etc_data.equals("")) {
            edit_arrayEtc = new ArrayList<Activity_etc_data>();
            String etc_array = gson.toJson(edit_arrayEtc, arrayEtc_list);
            editor.putString("etcData", etc_array);
            editor.commit();
        } else {

            edit_arrayEtc = gson.fromJson(etc_data, arrayEtc_list);
        }


        pos = intent.getIntExtra("position1", -1);

        byte[] arr = edit_arrayEtc.get(pos).getEtc_pic();
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        imageView.setImageBitmap(image);
        editEet.setText(edit_arrayEtc.get(pos).getEtcname());
        editEea.setText(edit_arrayEtc.get(pos).getEtcauth());
        editEer.setText(edit_arrayEtc.get(pos).getEtcwrite());

//
//
//        Toast myToast2 = Toast.makeText(this.getApplicationContext(), "수정 데이터 이동", Toast.LENGTH_SHORT);
//        myToast2.show();
//
//        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);

        btn_ees = findViewById(R.id.btn_edite_edit);
        btn_eec = findViewById(R.id.btn_edite_cancle);
        ImageButton btn_edite_img = (ImageButton) findViewById(R.id.btn_edite_img);

        btn_edite_img.setOnClickListener(this);
        btn_ees.setOnClickListener(this);
        btn_eec.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast myToast = Toast.makeText(this.getApplicationContext(), "추가버튼", Toast.LENGTH_SHORT);
        myToast.show();

        switch (v.getId()) {
            case R.id.btn_edite_edit:
                String eeTitle = editEet.getText().toString();
                String eeAuthor = editEea.getText().toString();
                String eeWrite = editEer.getText().toString();
                Toast myToast1 = Toast.makeText(this.getApplicationContext(), "수정버튼", Toast.LENGTH_SHORT);
                myToast1.show();

                sendBitmap = BitmapFactory.decodeResource(getResources(), R.id.btn_edite_img);
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
                Activity_etc_data activity_etc_data = new Activity_etc_data(eeTitle, eeAuthor, eeWrite, byteArray);
//                String edit_bdata = gson.toJson(activity_etc_data, Activity_etc_data.class);


                String etc_data = sharedPreferences.getString("etcData", "");

                if (etc_data.equals("")) {

                    edit_arrayEtc = new ArrayList<Activity_etc_data>();
                    String etc_array = gson.toJson(edit_arrayEtc, arrayEtc_list);
                    editor.putString("etcData", etc_array);
                    editor.commit();
                } else {

                    edit_arrayEtc = gson.fromJson(etc_data, arrayEtc_list);
                }

                edit_arrayEtc.set(pos, activity_etc_data);
                String arraylist = gson.toJson(edit_arrayEtc, arrayEtc_list);
                editor.putString("etcData", arraylist);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Activity_etc_list.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_edite_cancle:
                Intent intent1 = new Intent(getApplicationContext(), Activity_etc_list.class);
                startActivity(intent1);
                Toast myToast2 = Toast.makeText(this.getApplicationContext(), "수정취소", Toast.LENGTH_SHORT);
                myToast2.show();
                finish();

                break;
            case R.id.btn_edite_img:
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


