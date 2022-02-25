package com.nova.cstorage.book;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.MainActivity;
import com.nova.cstorage.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.sql.SQLData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_book_list extends AppCompatActivity implements View.OnClickListener, Activity_book_adapter.OnListItemSelectedInterface {

    SQLiteDatabase db;
    static final String DB_NAME = "database";
    static final String TABLE_NAME = "bookData";

    EditText bookSearch;

    ArrayList<BookVolumData> bookVolumData;
    private ArrayList<Activity_book_data> arrayBook;
    //    private ArrayList<rankData> ranklist = new ArrayList<>();
    private Activity_book_adapter adapter;
    ViewPager2 viewPager2;
    RecyclerView recyclerView;
    Gson gson = new GsonBuilder().create();
    String SharedPreFile = "cstorage_bookData";
    final Context context = this;
    Timer timer;
    int rankPage;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2500; // time in milliseconds between successive task executions.

    Type arrayBook_list = new TypeToken<ArrayList<Activity_book_data>>() {
    }.getType();

    BookDatabaseHelper myDb;
    TextView sqlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        bookSearch = findViewById(R.id.book_search);
        viewPager2 = findViewById(R.id.viewp_rank);
        sqlData = findViewById(R.id.bookListTitle);
        int rank = 0;
        int i;


        myDb = new BookDatabaseHelper(Activity_book_list.this);

        myDb.addBook(1000, "안티프레질");
        myDb.addBook(200, "블랙스완");
        myDb.addBook(500, "스킨인더게임");
//
        myDb.deleteALLMyNumber(db);
        printTable();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (rankPage == 3) {
                    rankPage = 0;
                }
                viewPager2.setCurrentItem(rankPage++, true);
            }
        };


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        recyclerView = findViewById(R.id.book_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreFile, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        String book_data = sharedPreferences.getString("bookData", "");
        if (book_data.equals("")) {
            arrayBook = new ArrayList<Activity_book_data>();
            String book_array = gson.toJson(arrayBook, arrayBook_list);
            editor.putString("bookData", book_array);
            editor.commit();
        } else {

            arrayBook = gson.fromJson(book_data, arrayBook_list);
        }

        Log.d(TAG, "arraybook : " + "사이즈" + arrayBook.size());
        adapter = new Activity_book_adapter(context, this, arrayBook);
        recyclerView.setAdapter(adapter);

        Button Add_book = (Button) findViewById(R.id.btn_b_add);
        Button Add_book_back = (Button) findViewById(R.id.btn_b_back);
        Button book_search = (Button) findViewById(R.id.btn_b_search);

        Add_book.setOnClickListener(this);
        Add_book_back.setOnClickListener(this);
        book_search.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_b_add) {
            Intent intent = new Intent(getApplicationContext(), Activity_add_book.class);
            startActivity(intent);
            finish();

        } else if (v.getId() == R.id.btn_b_back) {

            finish();
        } else if (v.getId() == R.id.btn_b_search) {
            String bsResult = bookSearch.getText().toString();
            Intent intentSearch = new Intent(getApplicationContext(), Activity_book_search.class);
            intentSearch.putExtra("bookSearch", bsResult);
            startActivity(intentSearch);
        }
    }

    @Override
    public void onItemSelected(View v, int position) {
        Activity_book_adapter.CustomViewHolder viewHolder = (Activity_book_adapter.CustomViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
    }

    private void printTable() {
        db = myDb.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        bookVolumData = new ArrayList<BookVolumData>();

        while (cursor.moveToNext()) {
            bookVolumData.add(0, new BookVolumData(cursor.getInt(1), cursor.getString(0)));

        }

        Collections.sort(bookVolumData, new Comparator<BookVolumData>() {
            @Override
            public int compare(BookVolumData o1, BookVolumData o2) {
                return o2.getBookVolum() - o1.getBookVolum();
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });

        viewPager2.setAdapter(new rankAdapter(bookVolumData));
        cursor.close();
    }

}
