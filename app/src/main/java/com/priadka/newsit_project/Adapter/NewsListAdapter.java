package com.priadka.newsit_project.Adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.priadka.newsit_project.DTO.NewsDTO;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;
import com.priadka.newsit_project.fragment.FullStateFragment;

import java.util.List;

import static com.priadka.newsit_project.Constant.F_S_IMAGE_DATABASE;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>{
    private List<NewsDTO> data;
    private Context mainContext;

    public NewsListAdapter(List<NewsDTO> data) {this.data = data;}

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mainContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int position) {
        // Установка данных в заранее созданый шаблон новости
        final NewsDTO item = data.get(position);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(F_S_IMAGE_DATABASE).child(item.getImage());
        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(F_S_IMAGE_DATABASE).child(item.getImage());
                Glide.with(mainContext).using(new FirebaseImageLoader()).load(mStorageRef).into(holder.image);
            }
        });
        holder.id = item.getId();
        holder.title.setText(item.getTitle() + " (ID: " + item.getId() + ")");
        holder.date.setText(item.getDate());
        holder.rating.setText(String.format(item.getRating().toString()) );
        holder.num_comment.setText(String.format(item.getNumberComment().toString()) );
        // Обработчик нажатия на контретную новость
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("state_id", item.getId());
                bundle.putString("state_title", item.getTitle());
                bundle.putString("state_text", item.getText());
                bundle.putString("state_date", item.getDate());
                bundle.putInt("state_rating", item.getRating());
                bundle.putString("state_image", item.getImage());
                // Сохраняем данную информацию и передаем ее в FullStateFragment
                FragmentTransaction transaction;
                transaction = MainActivity.manager.beginTransaction();
                FullStateFragment fullStateFragment = new FullStateFragment();
                fullStateFragment.setArguments(bundle);
                // Анимация перехода
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.container, fullStateFragment);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                // Приминяем транзакцию - переход на FullStateFragment
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {return data.size();}

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView title; TextView date; ImageView image;
        TextView num_comment; TextView rating;
        int id;

        public NewsViewHolder(View itemView) {
            super(itemView);
            // Инициализация переменных
            cardView = (CardView) itemView.findViewById(R.id.cardViewNews);
            title = (TextView) itemView.findViewById(R.id.small_state_header);
            image = (ImageView) itemView.findViewById(R.id.small_state_image);
            date = (TextView) itemView.findViewById(R.id.small_state_date);
            num_comment = (TextView) itemView.findViewById(R.id.small_state_number_comments);
            rating = (TextView) itemView.findViewById(R.id.small_state_rating);
            if(image.getScaleType() != ImageView.ScaleType.CENTER_CROP)image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
