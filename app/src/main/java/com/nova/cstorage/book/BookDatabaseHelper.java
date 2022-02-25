package com.nova.cstorage.book;

import static android.content.ContentValues.TAG;
import static android.provider.BaseColumns._ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BookDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String databaseName = "Book.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "bookData";
    public static final String COLUMN_VOLUM = "book_volum";
    public static final String COLUMN_TITLE = "book_title";


    public BookDatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME
                + " (" + COLUMN_TITLE + " TEXT, "
                + COLUMN_VOLUM + " INTEGER); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addBook(int volum, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_VOLUM, volum);
        cv.put(COLUMN_TITLE, title);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {

        }


    }
//    public Cursor readRecordOrderByVolum() {
//        SQLiteDatabase db = getReadableDatabase();
//        String[] projection = {
//                _ID,
//                COLUMN_VOLUM, COLUMN_TITLE
//        };
//        String sortOrder = COLUMN_VOLUM + " DESC";
//
//        Cursor cursor = db.query(
//                TABLE_NAME,
//                projection, null,
//                null,
//                null,
//                null, sortOrder);
//        return cursor;
//
//    }

    public Cursor readAllBook() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select * from " + TABLE_NAME, null);

        String bookRank = "순위 리스트"+"\r\n"+"\r\n";
        String bookTitle = "수량"+"\r\n"+"\r\n";
        while (cursor.moveToNext()){
            bookRank += cursor.getInt(0) + "\r\n";
            bookTitle += cursor.getString(1) + "\r\n";
        }
//        cursor.close();
//        db.close();
        return cursor;
    }

    public void deleteALLMyNumber(SQLiteDatabase db) {
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM bookData");
        db.close();

    }

    public boolean updateData(String volum, String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VOLUM, volum);
        contentValues.put(COLUMN_TITLE, title);
        db.update(TABLE_NAME, contentValues, "COLUMN_VOLUM = ?", new String[]{volum});
        return true;
    }




}
