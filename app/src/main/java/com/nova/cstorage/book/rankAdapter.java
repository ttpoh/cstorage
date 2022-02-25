package com.nova.cstorage.book;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.nova.cstorage.R;

import java.util.ArrayList;
public class rankAdapter extends RecyclerView.Adapter<rankViewHolderPage> {

        private ArrayList<BookVolumData> bookVolumData;

    rankAdapter(ArrayList<BookVolumData> data) {
            this.bookVolumData = data;
        }

        @Override
        public rankViewHolderPage onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.book_rankviewpager, parent, false);
            return new rankViewHolderPage(view);
        }

        @Override
        public void onBindViewHolder(rankViewHolderPage holder, int position) {
            if (holder instanceof rankViewHolderPage) {
                rankViewHolderPage viewHolder = (rankViewHolderPage) holder;
                viewHolder.onBind(bookVolumData.get(position),position);
            }
        }

        @Override
        public int getItemCount() {
            return bookVolumData.size();

        }
    }


