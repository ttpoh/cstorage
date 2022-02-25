package com.nova.cstorage.etc;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;
import com.nova.cstorage.etc.Activity_etc_adapter;
import com.nova.cstorage.etc.Activity_etc_data;
import com.nova.cstorage.etc.Activity_etc_edit;
import com.nova.cstorage.etc.Activity_etc_data;
import com.nova.cstorage.etc.Activity_etc_list;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Activity_etc_adapter extends RecyclerView.Adapter<Activity_etc_adapter.CustomViewHolder> {

    private ArrayList<Activity_etc_data> arrayEtc;
    Context context;
    String SharedPreFile = "cstorage_etcData";
    Gson gson;
    Type arrayEtc_list = new TypeToken<ArrayList<Activity_etc_data>>() {
    }.getType();
    int pos;

    public Activity_etc_adapter(Context context, Activity_etc_adapter.OnListItemSelectedInterface listener, ArrayList<Activity_etc_data> arrayEtc) {


        this.arrayEtc = arrayEtc;
        this.context = context;
        this.mListener = listener;
    }


    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private Activity_etc_adapter.OnListItemSelectedInterface mListener;


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView etc_list_img;
        protected TextView etclist_title;
        protected TextView etclist_author;
        protected TextView etclist_review;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.etc_list_img = itemView.findViewById(R.id.etc_list_img);
            this.etclist_title = itemView.findViewById(R.id.etclist_title);
            this.etclist_author = itemView.findViewById(R.id.etclist_date);
            this.etclist_review = itemView.findViewById(R.id.etclist_review);
            itemView.setOnClickListener(new View.OnClickListener() {
                //아이템 클릭 후 수정 페이지로 이동.
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();
                    mListener.onItemSelected(v, getAdapterPosition());
                    if (pos != RecyclerView.NO_POSITION) {

                        Intent intent = new Intent(context, Activity_etc_edit.class);
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
                                String etcDataD = sharedPreferences.getString("etcData", "");

                                arrayEtc = gson.fromJson(etcDataD, arrayEtc_list);
                                arrayEtc.remove(pos);
                                String detc = gson.toJson(arrayEtc, arrayEtc_list);
                                editor.putString("etcData", detc);
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

        public void onBind(Activity_etc_data item) {
            Log.e("바인드데이터", "데이터");


            byte[] arr = item.getEtc_pic();
            Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);


            etc_list_img.setImageBitmap(image);
            etclist_title.setText(item.getEtcname());
            etclist_author.setText(item.getEtcauth());
            etclist_review.setText(item.getEtcwrite());

        }
    }

    @NonNull
    @Override
    public Activity_etc_adapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.e("뷰홀더", "생성");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.etc_item, viewGroup, false);
        Activity_etc_adapter.CustomViewHolder viewholder = new Activity_etc_adapter.CustomViewHolder(view);

        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull Activity_etc_adapter.CustomViewHolder viewholder, int position) {

        Log.d("바인드뷰홀더", "바뷰홀더");
        viewholder.onBind(arrayEtc.get(position));

    }

    @Override
    public int getItemCount() {
        Log.d("아이템수", String.valueOf(arrayEtc.size()));
        return (null != arrayEtc ? arrayEtc.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void remove(int position) {
        try {
            arrayEtc.remove(position);
            notifyItemRemoved(position);

        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();

        }
    }

}
