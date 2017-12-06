package com.priadka.newsit_project.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.priadka.newsit_project.DTO.CommentDTO;
import com.priadka.newsit_project.R;

import java.util.List;

import static com.priadka.newsit_project.MainActivity.getResId;

// Класс выполняющий "подгрузку" данных из списка новостей в CardView
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>{
    private List<CommentDTO> data;
    //private Context mainContext;

    public CommentListAdapter(List<CommentDTO> data) {this.data = data;}

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //mainContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        // Установка данных в заранее созданый шаблон новости
        final CommentDTO item = data.get(position);
        holder.image.setImageResource(getResId("avatar_" + item.getCommImage(), R.drawable.class));
        holder.date.setText(item.getCommDate());
        holder.author.setText(item.getCommAuthor());
        holder.text.setText(item.getCommText());
    }

    @Override
    public int getItemCount() {return data.size();}

    public static class CommentViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView author, text, date;
        ImageView image;

        public CommentViewHolder(View itemView) {
            super(itemView);
            // Инициализация переменных
            cardView = (CardView) itemView.findViewById(R.id.cardViewNews);
            image = (ImageView) itemView.findViewById(R.id.user_avatar);
            date = (TextView) itemView.findViewById(R.id.comment_date);
            author = (TextView) itemView.findViewById(R.id.user_name);
            text = (TextView) itemView.findViewById(R.id.user_comment);
        }
    }
}
