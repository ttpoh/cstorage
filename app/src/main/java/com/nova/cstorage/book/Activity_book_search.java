package com.nova.cstorage.book;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nova.cstorage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Activity_book_search extends AppCompatActivity implements View.OnClickListener {
    StringBuilder searchResult;
    BufferedReader br;
    String title, author, postdate;
    String img;
    bookSearchAdapter searchAdapter;
    Context context;
    ArrayList<BookSearchData> searchList;

    private RecyclerView searchview;
    int itemCount;
    BookSearchData bookSearchData;
    EditText searchBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);

        searchBook = (EditText) findViewById(R.id.bookSearch_result);


        searchview = (RecyclerView) findViewById(R.id.bookSearch_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        searchview.setLayoutManager(linearLayoutManager);

        Button book_search = (Button) findViewById(R.id.btn_b_search);
        Button book_search_back = (Button) findViewById(R.id.btn_b_searchBack);

        book_search.setOnClickListener(this);
        book_search_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_b_search) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBook.getWindowToken(),0);
            final String clientId = "0RSZi8t0hfhbIvGMaJOl";//?????????????????? ??????????????? ????????????";
            final String clientSecret = "LHI61q3lFZ";//?????????????????? ??????????????? ????????????";
            final int display = 6; // ???????????? ??????????????? ???

            // ???????????? ????????? Thread ?????? ??????
            new Thread() {

                @Override
                public void run() {
                    try {
                        String bookSearch = searchBook.getText().toString();

                        String text = URLEncoder.encode(bookSearch, "UTF-8");
                        String apiURL = "https://openapi.naver.com/v1/search/book?query=" + text + "&display=" + display + "&"; // json ??????

                        URL url = new URL(apiURL);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("X-Naver-Client-Id", clientId);
                        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                        con.connect();

                        int responseCode = con.getResponseCode();


                        if (responseCode == 200) { // ?????? ??????
                            br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        } else {  // ?????? ??????
                            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        }

                        searchResult = new StringBuilder();
                        String inputLine;
                        while ((inputLine = br.readLine()) != null) {
                            searchResult.append(inputLine + "\n");
//                        Log.d(TAG, "???????????????" + searchResult);
                        }
                        br.close();
                        con.disconnect();

                        String data = searchResult.toString();
                        Log.d(TAG, "????????? json?????????" + data);
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            JSONArray bookArray = jsonObject.getJSONArray("items");
                            searchList = new ArrayList<BookSearchData>();
                            for (int i = 0; i < bookArray.length(); i++) {
                                JSONObject bookObject = bookArray.getJSONObject(i);
                                Log.d(TAG, "for???" + bookObject.length());
                                bookSearchData = new BookSearchData();
                                bookSearchData.setTitle(bookObject.getString("title"));
                                bookSearchData.setBook_pic(bookObject.getString("image"));
                                bookSearchData.setAuthor(bookObject.getString("author"));


                                searchList.add(bookSearchData);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        searchAdapter = new bookSearchAdapter(context, searchList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                searchview.setAdapter(searchAdapter);

                            }
                        });
                    } catch (Exception e) {
                        Log.d(TAG, "error : " + e);
                    }


                }

            }.start();
        } else if (v.getId() == R.id.btn_b_searchBack) {
            Intent intent = new Intent(v.getContext(), Activity_book_list.class);
            startActivity(intent);
            finish();
        }
    }

//    @Override
//    public void onItemSelected(View v, int position) {
//        bookSearchAdapter.CustomViewHolder viewHolder = (bookSearchAdapter.CustomViewHolder) searchview.findViewHolderForAdapterPosition(position);
//    }


}

