package com.nova.cstorage.book;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.gson.reflect.TypeToken;
import com.nova.cstorage.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class bookSearchAdapter extends RecyclerView.Adapter<bookSearchAdapter.CustomViewHolder> {

    private ArrayList<BookSearchData> arraySearchBook;
    BookSearchData bookSearchData;
    String title, author;
    Context context;
    int pos;
    Gson gson;
    private OnItemClickListener mListener =null;

    public bookSearchAdapter(Context context, ArrayList<BookSearchData> arraySearchBook) {


        this.arraySearchBook = arraySearchBook;
        this.context = context;
//        this.mListener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView book_search_img;
        protected TextView booksearch_title;
        protected TextView booksearch_author;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.book_search_img = itemView.findViewById(R.id.book_search_img);
            this.booksearch_title = itemView.findViewById(R.id.booksearch_title);
            this.booksearch_author = itemView.findViewById(R.id.booksearch_author);

            itemView.setOnClickListener(new View.OnClickListener() {
                //아이템 클릭 후 수정 페이지로 이동.
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {

                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                        bookSearchData = new BookSearchData();
                        Intent intent = new Intent(v.getContext(), Activity_searchAdd_book.class);

                        intent.putExtra("position1", pos);
                        intent.putExtra("searchT", arraySearchBook.get(pos).getTitle());

                        intent.putExtra("searchA", arraySearchBook.get(pos).getAuthor());
                        intent.putExtra("searchP", arraySearchBook.get(pos).getBookpic());
                        Log.d(TAG, "검색 결과 추가 : " + arraySearchBook.get(pos).getBookpic());
                        (v.getContext()).startActivity(intent);
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

        public void onBind(BookSearchData item) {
            Log.e("바인드데이터", "바인드데이터" + item);


            String imageUrl = item.getBookpic();
            Log.e("바인드데이터", "타이틀" + item.getTitle());
            booksearch_title.setText(Html.fromHtml(item.getTitle()));
            booksearch_author.setText(Html.fromHtml(item.getAuthor()));
//            book_search_img.setTag(item);
            if (item.getBookpic().isEmpty()) {
                book_search_img.setImageResource(R.drawable.book);
            } else {
                Picasso.get().load(imageUrl).into(book_search_img);
            }
        }
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.e("뷰홀더", "생성");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_search_item, viewGroup, false);
        CustomViewHolder viewholder = new CustomViewHolder(view);

        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        Log.d("바인드뷰홀더", "바뷰홀더" + position);
        viewholder.onBind(arraySearchBook.get(position));

    }

    @Override
    public int getItemCount() {
        Log.d("아이템수", String.valueOf(arraySearchBook.size()));
        return (null != arraySearchBook ? arraySearchBook.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}

