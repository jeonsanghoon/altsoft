package com.altsoft.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.altsoft.loggalapp.R;

public class BannerRecyclerView extends RecyclerView.Adapter<BannerRecyclerView.RecyclerViewHolder> {
    private Context mContext;
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        mContext = parent.getContext();
        RecyclerViewHolder holder = new RecyclerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconImageView;
        public TextView titleTextView;
        public TextView descTextView;
        public TextView userNameView;
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            iconImageView = (ImageView) itemView.findViewById(R.id.imageView1) ;
            titleTextView = (TextView) itemView.findViewById(R.id.textView1) ;
            descTextView = (TextView) itemView.findViewById(R.id.textView2) ;
            userNameView = (TextView) itemView.findViewById(R.id.textView3) ;
        }
    }

}
