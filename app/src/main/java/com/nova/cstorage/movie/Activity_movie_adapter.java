package com.nova.cstorage.movie;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;
import com.nova.cstorage.movie.Activity_movie_adapter;
import com.nova.cstorage.movie.Activity_movie_data;
import com.nova.cstorage.movie.Activity_movie_edit;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_movie_adapter extends RecyclerView.Adapter<Activity_movie_adapter.CustomViewHolder> {

    private ArrayList<Activity_movie_data> arraymovie;
    Context context;
    String SharedPreFile = "cstorage_movieData";
    Gson gson;
    Type arraymovie_list = new TypeToken<ArrayList<Activity_movie_data>>() {
    }.getType();
    int pos;

    public Activity_movie_adapter(Context context, Activity_movie_adapter.OnListItemSelectedInterface listener, ArrayList<Activity_movie_data> arraymovie) {


        this.arraymovie = arraymovie;
        this.context = context;
        this.mListener = listener;
    }


    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private Activity_movie_adapter.OnListItemSelectedInterface mListener;


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView movie_list_img;
        protected TextView movielist_title;
        protected TextView movielist_director;
        protected TextView movielist_review;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.movie_list_img = itemView.findViewById(R.id.movie_list_img);
            this.movielist_title = itemView.findViewById(R.id.movielist_title);
            this.movielist_director = itemView.findViewById(R.id.movielist_director);
            this.movielist_review = itemView.findViewById(R.id.movielist_review);
            itemView.setOnClickListener(new View.OnClickListener() {
                //아이템 클릭 후 수정 페이지로 이동.
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();
                    mListener.onItemSelected(v, getAdapterPosition());
                    if (pos != RecyclerView.NO_POSITION) {

                        Intent intent = new Intent(context, Activity_movie_edit.class);
                        intent.putExtra("position1", pos);
                        (context).startActivity(intent);
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
//                                remove(getLayoutPosition());
                                pos = getAdapterPosition();
                                gson = new Gson();

                                SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreFile, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String movieDataD = sharedPreferences.getString("movieData", "");

                                arraymovie = gson.fromJson(movieDataD, arraymovie_list);
                                arraymovie.remove(pos);
                                String dmovie = gson.toJson(arraymovie, arraymovie_list);
                                editor.putString("movieData", dmovie);
                                editor.commit();
                                notifyDataSetChanged();

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

        public void onBind(Activity_movie_data item) {
            Log.e("바인드데이터", "데이터");


            byte[] arr = item.getMovie_pic();
            Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);


            movie_list_img.setImageBitmap(image);
            movielist_title.setText(item.getMoviename());
            movielist_director.setText(item.getMovieauth());
            movielist_review.setText(item.getMoviewrite());

        }
    }

    @NonNull
    @Override
    public Activity_movie_adapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.e("뷰홀더", "생성");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item, viewGroup, false);
        Activity_movie_adapter.CustomViewHolder viewholder = new Activity_movie_adapter.CustomViewHolder(view);

        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull Activity_movie_adapter.CustomViewHolder viewholder, int position) {

        Log.d("바인드뷰홀더", "바뷰홀더");
        viewholder.onBind(arraymovie.get(position));

    }

    @Override
    public int getItemCount() {
        Log.d("아이템수", String.valueOf(arraymovie.size()));
        return (null != arraymovie ? arraymovie.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void remove(int position) {
        try {
            arraymovie.remove(position);
            notifyItemRemoved(position);

        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();

        }
    }

}
