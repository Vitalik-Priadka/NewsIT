package com.priadka.newsit_project.Adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.priadka.newsit_project.DTO.NewsDTO;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;
import com.priadka.newsit_project.fragment.FullStateFragment;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>{
    private List<NewsDTO> data;

    public NewsListAdapter(List<NewsDTO> data) {this.data = data;}

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int position) {
        final NewsDTO item = data.get(position);
        holder.id = item.getId();
        holder.title.setText(item.getTitle() + "\nID:" + item.getId());
        //MainActivity mainActivity = new MainActivity();
        //Picasso.with(mainActivity).load(item.getImage()).into(holder.image);
        //holder.image.setImageResource( MainActivity.getResId("avatar_" + item.getImage(), R.drawable.class) );
        holder.date.setText(item.getDate());
        holder.rating.setText(String.format(item.getRating().toString()) );
        holder.num_comment.setText(String.format(item.getNumberComment().toString()) );

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("state_id", item.getId());
                bundle.putString("state_title", item.getTitle());
                bundle.putString("state_text", item.getText());
                bundle.putInt("state_image", 2);
                bundle.putString("state_date", item.getDate());
                bundle.putInt("state_rating", item.getRating());

                FragmentTransaction transaction;
                transaction = MainActivity.manager.beginTransaction();
                FullStateFragment fullStateFragment = new FullStateFragment();
                fullStateFragment.setArguments(bundle);
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.container, fullStateFragment);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView title; TextView date; ImageView image;
        TextView num_comment; TextView rating;
        int id;

        public NewsViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewNews);
            title = (TextView) itemView.findViewById(R.id.small_state_header);
            image = (ImageView) itemView.findViewById(R.id.small_state_image);
            date = (TextView) itemView.findViewById(R.id.small_state_date);
            num_comment = (TextView) itemView.findViewById(R.id.small_state_number_comments);
            rating = (TextView) itemView.findViewById(R.id.small_state_rating);
        }
    }
}
