package com.nova.cstorage.map;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nova.cstorage.R;
import com.nova.cstorage.book.Activity_add_book;
import com.nova.cstorage.book.BookSearchData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MapSearchAdapter extends RecyclerView.Adapter<com.nova.cstorage.map.MapSearchAdapter.CustomViewHolder> {

    private ArrayList<MapPlaceData> arraySearchMap;
    MapPlaceData mapSearchData;
    String title, author;
    Context context;
    int pos;
    Gson gson;
    private com.nova.cstorage.book.bookSearchAdapter.OnItemClickListener mListener =null;

    public MapSearchAdapter(Context context, ArrayList<MapPlaceData> arraySearchMap) {


        this.arraySearchMap = arraySearchMap;
        this.context = context;
//        this.mListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView mapsearch_place;
        protected TextView mapsearch_category;
        protected TextView mapsearch_address;
        protected TextView mapsearch_phone;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mapsearch_place = itemView.findViewById(R.id.search_title);
            this.mapsearch_category = itemView.findViewById(R.id.search_category);
            this.mapsearch_address = itemView.findViewById(R.id.search_address);
            this.mapsearch_phone = itemView.findViewById(R.id.search_phone);

            itemView.setOnClickListener(new View.OnClickListener() {
                //아이템 클릭 후 수정 페이지로 이동.
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {

                        if(mListener !=null){
                            mListener.onItemClick(v, pos);
                        }
                        v.getContext();
                        String detailPage = arraySearchMap.get(pos).getUrl();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailPage));
                        v.getContext().startActivity(browserIntent);
//                        mapSearchData = new MapPlaceData();
//                        Intent intent = new Intent(v.getContext(), Activity_add_book.class);
//
//                        intent.putExtra("position1", pos);
//                        intent.putExtra("searchT", arraySearchMap.get(pos).getTitle());

//                        intent.putExtra("searchA", arraySearchMap.get(pos).getAuthor());
//                        intent.putExtra("searchP", arraySearchMap.get(pos).getBookpic());
//                        (v.getContext()).startActivity(intent);
                    }

                }
            });
            //삭제 다이얼로그 구현
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String st[] = {"1.예", "2.아니오"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("삭제하시겠습니까?");
                    builder.setItems(st, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (i == 0) {
                                pos = getAdapterPosition();
                                gson = new Gson();


                                Toast.makeText(context, "예", Toast.LENGTH_SHORT).show();
                            } else if (i == 1) {

                                Toast.makeText(context, "아니오", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }
            });
        }

        public void onBind(MapPlaceData item) {
            Log.e("바인드데이터", "바인드데이터" + item);


//            String imageUrl = item.getBookpic();
            Log.e("바인드데이터", "타이틀" + item.getPhone());
            mapsearch_place.setText(item.getPlaceName());
            mapsearch_category.setText(item.getCategory());
            mapsearch_address.setText(item.getAddress());
            mapsearch_phone.setText(item.getPhone());
//            book_search_img.setTag(item);
//            Picasso.get()
//                    .load(imageUrl)
//                    .into(mapsearch_img);
        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.e("뷰홀더", "생성");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchplace_item, viewGroup, false);
        CustomViewHolder viewholder = new CustomViewHolder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        Log.d("바인드뷰홀더", "바뷰홀더" + position);
        viewholder.onBind(arraySearchMap.get(position));
    }



    @Override
    public int getItemCount() {
        Log.d("아이템수", String.valueOf(arraySearchMap.size()));
        return (null != arraySearchMap ? arraySearchMap.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}

