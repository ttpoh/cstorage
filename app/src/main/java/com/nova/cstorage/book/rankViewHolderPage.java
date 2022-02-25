package com.nova.cstorage.book;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nova.cstorage.R;

public class rankViewHolderPage extends RecyclerView.ViewHolder {

    private TextView rankBooktitle;
    private TextView rankBookrank;
    private RelativeLayout rl_layout;

    BookVolumData data;

    rankViewHolderPage(View itemView) {
        super(itemView);

        rankBookrank = itemView.findViewById(R.id.rankbook_rank);
        rankBooktitle = itemView.findViewById(R.id.rankbook_title);
        rl_layout = itemView.findViewById(R.id.rl_layout);

    }

    public void onBind(BookVolumData data, int pos) {
        this.data = data;
        rankBookrank.setText(String.valueOf(pos+1));
        rankBooktitle.setText(data.getBookTitle());

//        rl_layout.setBackgroundResource();

    }
}