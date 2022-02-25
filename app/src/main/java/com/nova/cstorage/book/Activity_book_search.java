package com.nova.cstorage.book;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Activity_book_search extends AppCompatActivity implements View.OnClickListener, bookSearchAdapter.OnListItemSelectedInterface {
    StringBuilder searchResult;
    BufferedReader br;
    String title, author, postdate;
    bookSearchAdapter searchAdapter;
    Context context;
    ArrayList<BookSearchData> searchList;
    //    private OnListItemSelectedInterface listener;
    private RecyclerView searchview;
    int itemCount;
    BookSearchData bookSearchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);
        Intent intent = getIntent();
        EditText searchBook = (EditText) findViewById(R.id.bookSearch_result);
        String bookSearch = intent.getStringExtra("bookSearch");
        searchBook.setText(bookSearch);

        searchNaver(bookSearch);

        searchview = (RecyclerView) findViewById(R.id.bookSearch_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        searchview.setLayoutManager(linearLayoutManager);
//            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String blog_url = mMyAdapter.getItem(position).getBloglink();
//                    Intent intent = new Intent(getApplicationContext(), InternetWebView.class);
//                    intent.putExtra("blog_url", blog_url);
//                    startActivity(intent);
//
//                }
//            });


    }


    public void searchNaver(final String bookSearch) {
        final String clientId = "0RSZi8t0hfhbIvGMaJOl";//애플리케이션 클라이언트 아이디값";
        final String clientSecret = "LHI61q3lFZ";//애플리케이션 클라이언트 시크릿값";
        final int display = 5; // 보여지는 검색결과의 수

        // 네트워크 연결은 Thread 생성 필요
        new Thread() {

            @Override
            public void run() {
                try {
                    String text = URLEncoder.encode(bookSearch, "UTF-8");
                    String apiURL = "https://openapi.naver.com/v1/search/book?query=" + text + "&display=" + display + "&"; // json 결과

                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    con.connect();

                    int responseCode = con.getResponseCode();


                    if (responseCode == 200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }

                    searchResult = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        searchResult.append(inputLine + "\n");
//                        Log.d(TAG, "스트링버퍼" + searchResult);
                    }
                    br.close();
                    con.disconnect();

                    String data = searchResult.toString();

                    try {


//                    String[] array = data.split("\"");
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray bookArray = jsonObject.getJSONArray("items");
//                    title = new String[display];
//                    author = new String[display];
//                    itemCount = 0;
                        searchList = new ArrayList<BookSearchData>();
                        for (int i = 0; i < bookArray.length(); i++) {
                            JSONObject bookObject = bookArray.getJSONObject(i);
                            Log.d(TAG, "for문" + bookObject.length());
                            BookSearchData bookSearchData = new BookSearchData(title, author);
                            bookSearchData.setTitle(bookObject.getString("title"));
                            bookSearchData.setAuthor(bookObject.getString("author"));
                            Log.d(TAG, "title : " + bookObject.getString("title"));
                            searchList.add(bookSearchData);
                            Log.d(TAG, "searchlist" + searchList.size());
                        }
                        searchAdapter = new bookSearchAdapter(context, searchList);
                        searchview.setAdapter(searchAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            DataAdd();

                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "error : " + e);
                }

            }
        }.

                start();


    }

//
//    public void DataAdd() {
//        for (int i = 0; i < itemCount; i++) {
//
//            bookSearchData = new BookSearchData(Html.fromHtml(title[i]).toString(), Html.fromHtml(author[i]).toString());
//            Log.d(TAG, "검색데이터클래스 " + bookSearchData);
//            searchList.add(bookSearchData);


//                            for (int i = 0; i < itemCount; i++) {
//                                Log.d(TAG, "title : " + title);

//                                searchAdapter.addItem(Html.fromHtml(title[i]).toString(),
//                        Html.fromHtml(description[i]).toString(),
//                        Html.fromHtml(bloggername[i]).toString(),
//                        Html.fromHtml(postdate[i]).toString(),
//                                        Html.fromHtml(author[i]).toString());


    @Override
    public void onItemSelected(View v, int position) {
        Activity_book_adapter.CustomViewHolder viewHolder = (Activity_book_adapter.CustomViewHolder) searchview.findViewHolderForAdapterPosition(position);
    }

    @Override
    public void onClick(View v) {

    }
}

