package com.nova.cstorage.book;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
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
import com.nova.cstorage.todolist.todoAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class Activity_add_book extends AppCompatActivity implements View.OnClickListener {


    EditText input_bookauthor;
    EditText input_booktitle;
    EditText input_bookreview;

    private static final int REQUEST_CODE = 0;
    Bitmap sendBitmap;
    Bitmap img;
    private ImageButton imagebutton;

    ArrayList<BookSearchData> arraySearchBook;
    ArrayList<Activity_book_data> book_item;
    String bookData;
    String SharedPreFile = "cstorage_bookData";
    SharedPreferences sharedPreferences;
    BookSearchData bookSearchData;
    Gson gson;
    Context context;
    int pos;

    Type arrayBook_list = new TypeToken<ArrayList<Activity_book_data>>() {
    }.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_add);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", -1);

        arraySearchBook = new ArrayList<BookSearchData>();

        imagebutton = findViewById(R.id.btn_addb_img);

        input_booktitle = (EditText) findViewById(R.id.book_add_title);
        input_bookauthor = (EditText) findViewById(R.id.book_add_author);
        input_bookreview = (EditText) findViewById(R.id.book_add_review);

        Log.d(TAG, "검색 결과 추가 : " + arraySearchBook.size());
        String title = intent.getStringExtra("searchT");
        String author = intent.getStringExtra("searchA");
        String bookP = intent.getStringExtra("searchP");

        Button btn_addb_save = (Button) findViewById(R.id.btn_addb_save);
        Button btn_addb_cancle = (Button) findViewById(R.id.btn_addb_cancle);
        ImageButton btn_addb_img = (ImageButton) findViewById(R.id.btn_addb_img);


        sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);

        btn_addb_save.setOnClickListener(this);
        btn_addb_cancle.setOnClickListener(this);
        btn_addb_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_addb_save) {

            String book_title = input_booktitle.getText().toString();
            String book_author = input_bookauthor.getText().toString();
            String book_review = input_bookreview.getText().toString();


            if (book_review.equals("") && book_author.equals("") && book_title.equals("")) {//빈값이 넘어올때의 처리
                Toast myToast3 = Toast.makeText(this.getApplicationContext(), "내용을 입력해 주세요.", Toast.LENGTH_SHORT);
                myToast3.show();
            } else {


                sendBitmap = BitmapFactory.decodeResource(getResources(), R.id.btn_addb_img);
                sendBitmap = ((BitmapDrawable) imagebutton.getDrawable()).getBitmap();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                float scale = (float) (1024 / (float) sendBitmap.getWidth());
                int image_w = (int) (sendBitmap.getWidth() * scale);
                int image_h = (int) (sendBitmap.getHeight() * scale);
                Bitmap resize = Bitmap.createScaledBitmap(sendBitmap, image_w, image_h, true);
                resize.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                gson = new Gson();
                Activity_book_data activity_book_data = new Activity_book_data(book_title, book_author, book_review, byteArray);

                String book_data = sharedPreferences.getString("bookData", "");

                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (book_data.equals("")) {

                    book_item = new ArrayList<Activity_book_data>();
                    String addBook = gson.toJson(book_item, arrayBook_list);
                    editor.putString("bookData", addBook);
                    editor.commit();
                } else {

                    book_item = gson.fromJson(book_data, arrayBook_list);
                }

                book_item.add(activity_book_data);
                String arraylist = gson.toJson(book_item, arrayBook_list);
                editor.putString("bookData", arraylist);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Activity_book_list.class);
                startActivity(intent);


                finish();
            }
        }
        if (v.getId() == R.id.btn_addb_cancle) {
            Intent intent = new Intent(getApplicationContext(), Activity_book_list.class);
            startActivity(intent);
            finish();
        }
        if (v.getId() == R.id.btn_addb_img) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

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

                    imagebutton.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
}





