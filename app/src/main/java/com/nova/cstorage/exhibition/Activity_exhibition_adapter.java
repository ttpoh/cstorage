package com.nova.cstorage.exhibition;

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

import java.lang.reflect.Type;
import java.util.ArrayList;


public class Activity_exhibition_adapter extends RecyclerView.Adapter<Activity_exhibition_adapter.CustomViewHolder> {

    private ArrayList<Activity_exhibition_data> arrayexhibition;
    Context context;
    String SharedPreFile = "cstorage_exhibitionData";
    Gson gson;
    Type arrayexhibition_list = new TypeToken<ArrayList<Activity_exhibition_data>>() {
    }.getType();
    int pos;

    public Activity_exhibition_adapter(Context context, OnListItemSelectedInterface listener, ArrayList<Activity_exhibition_data> arrayexhibition) {


        this.arrayexhibition = arrayexhibition;
        this.context = context;
        this.mListener = listener;
    }


    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView exhibition_list_img;
        protected TextView exhibitionlist_title;
        protected TextView exhibitionlist_author;
        protected TextView exhibitionlist_review;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.exhibition_list_img = itemView.findViewById(R.id.exhibition_list_img);
            this.exhibitionlist_title = itemView.findViewById(R.id.exhibitionlist_title);
            this.exhibitionlist_author = itemView.findViewById(R.id.exhibitionlist_author);
            this.exhibitionlist_review = itemView.findViewById(R.id.exhibitionlist_review);
            itemView.setOnClickListener(new View.OnClickListener() {
                //아이템 클릭 후 수정 페이지로 이동.
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();
                    mListener.onItemSelected(v, getAdapterPosition());
                    if (pos != RecyclerView.NO_POSITION) {

                        Intent intent = new Intent(context, Activity_exhibition_edit.class);
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
                                String exhibitionDataD = sharedPreferences.getString("exhibitionData", "");

                                arrayexhibition = gson.fromJson(exhibitionDataD, arrayexhibition_list);
                                arrayexhibition.remove(pos);
                                String dexhibition = gson.toJson(arrayexhibition, arrayexhibition_list);
                                editor.putString("exhibitionData", dexhibition);
                                editor.commit();
                                notifyDataSetChanged();

//                                Toast.makeText(context, "예", Toast.LENGTH_SHORT).show();
                            } else if (i == 1) {

//                                Toast.makeText(context, "아니오", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return true;
                }
            });
        }

        public void onBind(Activity_exhibition_data item) {
            Log.e("바인드데이터", "데이터");


            byte[] arr = item.getExhibition_pic();
            Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);


            exhibition_list_img.setImageBitmap(image);
            exhibitionlist_title.setText(item.getExhibitionname());
            exhibitionlist_author.setText(item.getExhibitionauth());
            exhibitionlist_review.setText(item.getExhibitionwrite());

        }
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.e("뷰홀더", "생성");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exhibition_item, viewGroup, false);
        CustomViewHolder viewholder = new CustomViewHolder(view);

        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        Log.d("바인드뷰홀더", "바뷰홀더");
        viewholder.onBind(arrayexhibition.get(position));

    }

    @Override
    public int getItemCount() {
        Log.d("아이템수", String.valueOf(arrayexhibition.size()));
        return (null != arrayexhibition ? arrayexhibition.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void remove(int position) {
        try {
            arrayexhibition.remove(position);
            notifyItemRemoved(position);

        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();

        }
    }

}
