package com.nova.cstorage.book;


import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;

public class Activity_book_edit extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE = 0;

    Button btn_bes;
    Button btn_bec;
    EditText editBet;
    EditText editBea;
    EditText editBer;
    Gson gson;
    String SharedPreFile = "cstorage_bookData";
    Bitmap sendBitmap, image;
    private ImageView imageView;
    SharedPreferences sharedPreferences;
    private Intent intent;
    Context context;
    ArrayList<Activity_book_data> edit_arrayBook;
    private Activity_book_adapter adapter;

    Type arrayBook_list = new TypeToken<ArrayList<Activity_book_data>>() {
    }.getType();
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_edit);

        intent = getIntent();
        editBet = (EditText) findViewById(R.id.book_edit_title);
        editBea = (EditText) findViewById(R.id.book_edit_author);
        editBer = (EditText) findViewById(R.id.book_edit_review);
        imageView = findViewById(R.id.btn_editb_img);


        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        gson = new Gson();

        String book_data = sharedPreferences.getString("bookData", "");
        Log.d("sharedReceive",book_data);
        if (book_data.equals("")) {
            edit_arrayBook = new ArrayList<Activity_book_data>();
            String book_array = gson.toJson(edit_arrayBook, arrayBook_list);
            editor.putString("bookData", book_array);
            editor.commit();
        } else {

            edit_arrayBook = gson.fromJson(book_data, arrayBook_list);
        }


        pos = intent.getIntExtra("position1", -1);

        byte[] arr = edit_arrayBook.get(pos).getBook_pic();
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        imageView.setImageBitmap(image);
        editBet.setText(edit_arrayBook.get(pos).getBookname());
        editBea.setText(edit_arrayBook.get(pos).getBookauth());
        editBer.setText(edit_arrayBook.get(pos).getBookwrite());


        btn_bes = findViewById(R.id.btn_editb_edit);
        btn_bec = findViewById(R.id.btn_editb_cancle);
        ImageButton btn_editb_img = (ImageButton) findViewById(R.id.btn_editb_img);

        btn_editb_img.setOnClickListener(this);
        btn_bes.setOnClickListener(this);
        btn_bec.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        Toast myToast = Toast.makeText(this.getApplicationContext(), "추가버튼", Toast.LENGTH_SHORT);
//        myToast.show();

        switch (v.getId()) {
            case R.id.btn_editb_edit:
                String ebTitle = editBet.getText().toString();
                String ebAuthor = editBea.getText().toString();
                String ebWrite = editBer.getText().toString();


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
                Activity_book_data activity_book_data = new Activity_book_data(ebTitle, ebAuthor, ebWrite, byteArray);
//                String edit_bdata = gson.toJson(activity_book_data, Activity_book_data.class);


                String book_data = sharedPreferences.getString("bookData", "");

                if (book_data.equals("")) {

                    edit_arrayBook = new ArrayList<Activity_book_data>();
                    String book_array = gson.toJson(edit_arrayBook, arrayBook_list);
                    editor.putString("bookData", book_array);
                    editor.commit();
                } else {

                    edit_arrayBook = gson.fromJson(book_data, arrayBook_list);
                }

                edit_arrayBook.set(pos, activity_book_data);
                String arraylist = gson.toJson(edit_arrayBook, arrayBook_list);
                editor.putString("bookData", arraylist);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Activity_book_list.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_editb_cancle:
                Intent intent1 = new Intent(getApplicationContext(), Activity_book_list.class);
                startActivity(intent1);
                Toast myToast2 = Toast.makeText(this.getApplicationContext(), "수정취소", Toast.LENGTH_SHORT);
                myToast2.show();
                finish();

                break;
            case R.id.btn_editb_img:
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


