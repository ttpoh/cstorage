package com.nova.cstorage;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.nova.cstorage.book.Activity_book_list;
import com.nova.cstorage.concert.Activity_concert_list;
import com.nova.cstorage.etc.Activity_etc_list;
import com.nova.cstorage.exhibition.Activity_exhibition_list;
import com.nova.cstorage.movie.Activity_movie_list;
import com.nova.cstorage.todolist.Activity_todolist;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    TextView result_logmail;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    SeekBar sb; // 음악 재생위치를 나타내는 시크바
    boolean isPlaying = false; // 재생중인지 확인할 변수
    MediaPlayer mp; // 음악 재생을 위한 객체

    ImageView addView;
    ArrayList<Drawable> addList = new ArrayList<>();
    ArrayList<Drawable> addViewPager = new ArrayList<>();
    Handler handler = new Handler();

    private ViewPager viewPager;
    private Main_ViewPagerAdapter pagerAdapter;

    private ImageButton bStart;
    private ImageButton bPause;
    private Button bRestart;
    private ImageButton bStop;

    int pos;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    ImageButton imgBtn[] = new ImageButton[5];

    class seekbarThread extends Thread {
        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            // 씨크바 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)
            while (isPlaying) {
                sb.setProgress(mp.getCurrentPosition());
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sb = (SeekBar) findViewById(R.id.seek_music);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                isPlaying = true;
                int seekPosition = seekBar.getProgress(); // 사용자가 움직여놓은 위치
                mp.seekTo(seekPosition);
                mp.start();
                new seekbarThread().start();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
                mp.pause();
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getMax() == progress) {
                    bStart.setVisibility(View.VISIBLE);
//                    bStop.setVisibility(View.INVISIBLE);
                    bPause.setVisibility(View.INVISIBLE);
//                    bRestart.setVisibility(View.INVISIBLE);
                    isPlaying = true;
                    mp.stop();
                }
            }
        });
        bStart = (ImageButton) findViewById(R.id.btn_m_start);
        bPause = (ImageButton) findViewById(R.id.btn_m_pause);
//        bRestart = (Button) findViewById(R.id.btn_m_restart);
        bStop = (ImageButton) findViewById(R.id.btn_m_stop);

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MediaPlayer 객체 초기화 , 재생
                mp = MediaPlayer.create(getApplicationContext(), // 현재 화면의 제어권자
                        R.raw.jazz); // 음악파일
                mp.setLooping(true); // true:무한반복
                mp.seekTo(pos); // 일시정지 시점으로 이동
                mp.start(); // 노래 재생 시작

                int a = mp.getDuration(); // 노래의 재생시간(miliSecond)
                sb.setMax(a);// 씨크바의 최대 범위를 노래의 재생시간으로 설정
                new seekbarThread().start(); // 씨크바 그려줄 쓰레드 시작
                isPlaying = true; // 씨크바 쓰레드 반복 하도록

                bStart.setVisibility(View.INVISIBLE);
                bStop.setVisibility(View.VISIBLE);
                bPause.setVisibility(View.VISIBLE);
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 음악 종료
                isPlaying = false; // 쓰레드 종료

                mp.stop(); // 멈춤
                mp.release(); // 자원 해제
                bStart.setVisibility(View.VISIBLE);
                bPause.setVisibility(View.INVISIBLE);
//                bRestart.setVisibility(View.INVISIBLE);
                bStop.setVisibility(View.INVISIBLE);

                sb.setProgress(0); // 씨크바 초기화
            }
        });

        bPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 일시중지
                pos = mp.getCurrentPosition();
                mp.pause(); // 일시중지
                bStart.setVisibility(View.VISIBLE);
                bPause.setVisibility(View.INVISIBLE);
//                bRestart.setVisibility(View.VISIBLE);
                isPlaying = false; // 쓰레드 정지
            }
        });
//        bRestart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 멈춘 지점부터 재시작
//                mp.seekTo(pos); // 일시정지 시점으로 이동
//                mp.start(); // 시작
//                bRestart.setVisibility(View.INVISIBLE);
//                bPause.setVisibility(View.VISIBLE);
//                isPlaying = true; // 재생하도록 flag 변경
//                new seekbarThread().start(); // 쓰레드 시작
//            }
//        });
        addViewPager.add(getDrawable(R.drawable.interpark));
        addViewPager.add(getDrawable(R.drawable.yes24));
        addViewPager.add(getDrawable(R.drawable.cgv));
        addViewPager.add(getDrawable(R.drawable.lotte));
        addViewPager.add(getDrawable(R.drawable.hotelscombine));

        viewPager = findViewById(R.id.addViewPager);
        pagerAdapter = new Main_ViewPagerAdapter(this, addViewPager);
        viewPager.setAdapter(pagerAdapter);

        imgBtn[0] = (ImageButton) findViewById(R.id.btn_addView);
        imgBtn[1] = (ImageButton) findViewById(R.id.btn_addView);
        imgBtn[2] = (ImageButton) findViewById(R.id.btn_addView);
        imgBtn[3] = (ImageButton) findViewById(R.id.btn_addView);
        imgBtn[4] = (ImageButton) findViewById(R.id.btn_addView);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                //현재 페이지가 4번이면 다시 처음으로 돌아가자.
                if (currentPage == 4) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_ham);
        actionBar.setHomeButtonEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Intent receive_intent = getIntent();

        String mail = receive_intent.getStringExtra("1");
        if (mail == null) {
//            result_logmail.setText("반갑습니다.");
        } else {
            mail = receive_intent.getStringExtra("1") + "님";
            result_logmail.setText(mail);
        }
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.ham_book) {

                    Intent intent = new Intent(MainActivity.this, Activity_book_list.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();

                } else if (item.getItemId() == R.id.ham_movie) {
                    Intent intent = new Intent(MainActivity.this, Activity_movie_list.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.ham_exhibition) {
                    Intent intent = new Intent(MainActivity.this, Activity_exhibition_list.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.ham_concert) {
                    Intent intent = new Intent(MainActivity.this, Activity_concert_list.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                } else if (item.getItemId() == R.id.ham_etc) {
                    Intent intent = new Intent(MainActivity.this, Activity_etc_list.class);
                    startActivity(intent);
                    drawerLayout.closeDrawers();
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainbar_obtion, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(navigationView);
                break;
            }
            case R.id.obtion_btn_search:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.naver.com"));
                startActivity(intent);
                break;
            case R.id.obtion_btn_todo:
                Intent todoIntent = new Intent(this, Activity_todolist.class);
                startActivity(todoIntent);

                break;
            case R.id.obtion_btn_cam:
                Log.d("카메라", "권한");
                int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                    Intent camintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(camintent);
                    break;
                }

        }
        return super.onOptionsItemSelected(item);
    }


}


