package com.nova.cstorage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class loadingActivity extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.loading);

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();


    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

//        ProgressDialog asyncDialog = new ProgressDialog(
//                loadingActivity.this);

        @Override
        protected void onPreExecute() {
//            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            asyncDialog.setMessage("로딩중입니다..");
//
//            // show dialog
//            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Thread.sleep(2000); // 2초 지속

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            asyncDialog.dismiss();
            Intent intent = new Intent(loadingActivity.this, MainActivity.class);

            startActivity( intent );
            finish();
//            Toast.makeText(loadingActivity.this, "로딩 완료", Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);
        }
    }
}


