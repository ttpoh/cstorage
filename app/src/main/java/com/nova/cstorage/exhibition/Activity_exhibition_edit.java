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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;
import com.nova.cstorage.exhibition.Activity_exhibition_adapter;
import com.nova.cstorage.exhibition.Activity_exhibition_data;
import com.nova.cstorage.exhibition.Activity_exhibition_list;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_exhibition_edit extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 0;

    Button btn_EXes;
    Button btn_EXec;
    EditText editEXet;
    EditText editEXea;
    EditText editEXer;
    Gson gson;
    String SharedPreFile = "cstorage_exhibitionData";
    Bitmap sendBitmap, image;
    private ImageView imageView;
    SharedPreferences sharedPreferences;
    private Intent intent;
    Context context;
    ArrayList<Activity_exhibition_data> edit_arrayExhibition;
    private Activity_exhibition_adapter adapter;

    Type arrayExhibition_list = new TypeToken<ArrayList<Activity_exhibition_data>>() {
    }.getType();
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exhibition_edit);

        intent = getIntent();
        editEXet = (EditText) findViewById(R.id.exhibition_edit_title);
        editEXea = (EditText) findViewById(R.id.exhibition_edit_author);
        editEXer = (EditText) findViewById(R.id.exhibition_edit_review);
        imageView = findViewById(R.id.btn_editEX_img);


        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        gson = new Gson();

        String exhibition_data = sharedPreferences.getString("exhibitionData", "");
        if (exhibition_data.equals("")) {
            edit_arrayExhibition = new ArrayList<Activity_exhibition_data>();
            String exhibition_array = gson.toJson(edit_arrayExhibition, arrayExhibition_list);
            editor.putString("exhibitionData", exhibition_array);
            editor.commit();
        } else {

            edit_arrayExhibition = gson.fromJson(exhibition_data, arrayExhibition_list);
        }


        pos = intent.getIntExtra("position1", -1);

        byte[] arr = edit_arrayExhibition.get(pos).getExhibition_pic();
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        imageView.setImageBitmap(image);
        editEXet.setText(edit_arrayExhibition.get(pos).getExhibitionname());
        editEXea.setText(edit_arrayExhibition.get(pos).getExhibitionauth());
        editEXer.setText(edit_arrayExhibition.get(pos).getExhibitionwrite());

//
//
//        Toast myToast2 = Toast.makeText(this.getApplicationContext(), "수정 데이터 이동", Toast.LENGTH_SHORT);
//        myToast2.show();
//
//        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);

        btn_EXes = findViewById(R.id.btn_editEX_edit);
        btn_EXec = findViewById(R.id.btn_editEX_cancle);
        ImageButton btn_editEX_img = (ImageButton) findViewById(R.id.btn_editEX_img);

        btn_editEX_img.setOnClickListener(this);
        btn_EXes.setOnClickListener(this);
        btn_EXec.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast myToast = Toast.makeText(this.getApplicationContext(), "추가버튼", Toast.LENGTH_SHORT);
        myToast.show();

        switch (v.getId()) {
            case R.id.btn_editEX_edit:
                String eEXTitle = editEXet.getText().toString();
                String eEXAuthor = editEXea.getText().toString();
                String eEXWrite = editEXer.getText().toString();
                Toast myToast1 = Toast.makeText(this.getApplicationContext(), "수정버튼", Toast.LENGTH_SHORT);
                myToast1.show();

                sendBitmap = BitmapFactory.decodeResource(getResources(), R.id.btn_editEX_img);
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
                Activity_exhibition_data activity_exhibition_data = new Activity_exhibition_data(eEXTitle, eEXAuthor, eEXWrite, byteArray);
//                String edit_bdata = gson.toJson(activity_exhibition_data, Activity_exhibition_data.class);


                String exhibition_data = sharedPreferences.getString("exhibitionData", "");

                if (exhibition_data.equals("")) {

                    edit_arrayExhibition = new ArrayList<Activity_exhibition_data>();
                    String exhibition_array = gson.toJson(edit_arrayExhibition, arrayExhibition_list);
                    editor.putString("exhibitionData", exhibition_array);
                    editor.commit();
                } else {

                    edit_arrayExhibition = gson.fromJson(exhibition_data, arrayExhibition_list);
                }

                edit_arrayExhibition.set(pos, activity_exhibition_data);
                String arraylist = gson.toJson(edit_arrayExhibition, arrayExhibition_list);
                editor.putString("exhibitionData", arraylist);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Activity_exhibition_list.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_editEX_cancle:
                Intent intent1 = new Intent(getApplicationContext(), Activity_exhibition_list.class);
                startActivity(intent1);
                Toast myToast2 = Toast.makeText(this.getApplicationContext(), "수정취소", Toast.LENGTH_SHORT);
                myToast2.show();
                finish();

                break;
            case R.id.btn_editEX_img:
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


