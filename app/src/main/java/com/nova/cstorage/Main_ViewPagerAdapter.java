package com.nova.cstorage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class Main_ViewPagerAdapter extends PagerAdapter {

    private Context context = null;
    ImageButton addView;
    ArrayList<Drawable> addViewPager = new ArrayList<>();

    // Context 를 전달받아 context 에 저장하는 생성자 추가.
    public Main_ViewPagerAdapter(Context context, ArrayList<Drawable> addViewPager) {
        this.addViewPager = addViewPager;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // position 값을 받아 주어진 위치에 페이지를 생성한다

        View view = null;

        if (context != null) {
            // LayoutInflater 를 통해 "/res/layout/page.xml" 을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.main_viewpager_item, container, false);

            addView = view.findViewById(R.id.btn_addView);
            addView.setImageDrawable(addViewPager.get(position));
            addView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (position == 0) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.interpark.com"));
                        (context).startActivity(intent);
                    } else if (position == 1) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.yes24.com"));
                        (context).startActivity(intent);
                    } else if (position == 2) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.cgv.co.kr"));
                        (context).startActivity(intent);
                    }else if (position ==3 ){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.lottecinema.co.kr"));
                        (context).startActivity(intent);
                    }else if (position ==4){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.hotelscombined.co.kr"));
                        (context).startActivity(intent);
                    }
                }

                });

                // 뷰페이저에 추가
            container.addView(view);


            }
            return view;
        }

        @Override
        public void destroyItem (@NonNull ViewGroup container,int position, @NonNull Object object){
            // position 값을 받아 주어진 위치의 페이지를 삭제한다
            container.removeView((View) object);
        }

        @Override
        public int getCount () {
            // 사용 가능한 뷰의 개수를 return 한다
            // 전체 페이지 수는 10개로 고정한다
            return addViewPager.size();
        }

        @Override
        public boolean isViewFromObject (@NonNull View view, @NonNull Object object){
            // 페이지 뷰가 생성된 페이지의 object key 와 같은지 확인한다
            // 해당 object key 는 instantiateItem 메소드에서 리턴시킨 오브젝트이다
            // 즉, 페이지의 뷰가 생성된 뷰인지 아닌지를 확인하는 것
            return view == object;
        }
    }