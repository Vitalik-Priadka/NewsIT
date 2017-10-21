package com.priadka.newsit_project.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.priadka.newsit_project.DTO.NewsDTO;
import com.priadka.newsit_project.R;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>{
    private List<NewsDTO> data;

    public NewsListAdapter(List<NewsDTO> data) {
        this.data = data;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsDTO item = data.get(position);
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.rating.setText(item.getRating().toString());
        holder.num_comment.setText(item.getNumberComment().toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView title; TextView date;
        TextView num_comment; TextView rating;


        public NewsViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewNews);
            title = (TextView) itemView.findViewById(R.id.small_state_header);
            date = (TextView) itemView.findViewById(R.id.small_state_date);
            num_comment = (TextView) itemView.findViewById(R.id.small_state_number_comments);
            rating = (TextView) itemView.findViewById(R.id.small_state_rating);
        }
    }
}
