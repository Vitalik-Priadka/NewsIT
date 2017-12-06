package com.priadka.newsit_project.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.priadka.newsit_project.Adapter.CommentListAdapter;
import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.DTO.CommentDTO;
import com.priadka.newsit_project.DTO.UserDTO;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.priadka.newsit_project.Constant.F_STATE;
import static com.priadka.newsit_project.Constant.F_STATE_COMMENTS;
import static com.priadka.newsit_project.Constant.F_STATE_COMMENT_AUTHOR;
import static com.priadka.newsit_project.Constant.F_STATE_COMMENT_DATE;
import static com.priadka.newsit_project.Constant.F_STATE_COMMENT_IMAGE;
import static com.priadka.newsit_project.Constant.F_STATE_COMMENT_TEXT;
import static com.priadka.newsit_project.Constant.F_S_IMAGE_DATABASE;
import static com.priadka.newsit_project.Constant.F_S_RATING;

// Класс "с логикой" фрагмента полной статьи
public class FullStateFragment extends Fragment {
    private ImageButton starButton, sendComment;
    private TextView rating;
    private EditText commentField;
    protected Context context;

    private UserDTO user;
    private List<CommentDTO> dataComment;
    private FirebaseAuth mAuth;
    private DatabaseReference myRefState;

    private String state_title, state_text, state_date, state_image;
    private int state_id, state_rating ;
    private boolean isLiked = false, isTime = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(Constant.FULL_STATE, container, false);
        // Получаем информацию от CardView (который был нажат)
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            state_id = bundle.getInt("state_id",0);
            if(state_id == 0){
                ((MainActivity)getActivity()).reloadPage();
            }
            state_title = bundle.getString("state_title");
            state_text = bundle.getString("state_text");
            state_date = bundle.getString("state_date");
            state_image = bundle.getString("state_image");
            state_rating = bundle.getInt("state_rating",0);
            state_rating = bundle.getInt("state_number_comment",0);
        }
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycleViewComment);
        rv.setLayoutManager( new LinearLayoutManager(context));
        rv.setAdapter(new CommentListAdapter(CommentList()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Инициализация переменных
        user = ((MainActivity)getActivity()).getUser();  mAuth = FirebaseAuth.getInstance();
        ImageView image = (ImageView) getActivity().findViewById(R.id.state_image);
        TextView title = (TextView) getActivity().findViewById(R.id.state_header);
        WebView text = (WebView) getActivity().findViewById(R.id.state_text);
        TextView date = (TextView) getActivity().findViewById(R.id.state_date);
        rating = (TextView) getActivity().findViewById(R.id.state_rating);
        starButton  = (ImageButton) getActivity().findViewById(R.id.state_star_like);
        LinearLayout commentBlock = (LinearLayout) getActivity().findViewById(R.id.commentBlock);

        // Установка значений полученых переменных
        title.setText(state_title);
        text.setBackgroundColor(Color.TRANSPARENT);
        String color;
        if(((MainActivity)getActivity()).getCurrentTheme() == 0){
            color = "color: #000;";
        }
        else color = "color: #fff;";
        // Веб поле для текста - ведь тут есть выравнивание по ширине
        text.loadData("<p style=\"text-align: justify; white-space: pre-line; "+ color +" font-size: 15px; font-family: serif;\">"+ state_text + "</p>", "text/html", "UTF-8");
        if(image.getScaleType() != ImageView.ScaleType.CENTER_CROP)image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child(F_S_IMAGE_DATABASE).child(state_image);
        Glide.with(getContext()).using(new FirebaseImageLoader()).load(mStorageRef).into(image);
        date.setText(state_date);
        rating.setText(String.valueOf(state_rating));

        // Смотрим на состояние пользователя
        if(mAuth.getCurrentUser() != null && ((MainActivity)getActivity()).getUser() != null){
            // Он активен - вкл. поле для комментирования и изменение закладки
            commentField = (EditText) getActivity().findViewById(R.id.state_comment_field);
            sendComment = (ImageButton) getActivity().findViewById(R.id.sendComment);
            isLiked = user.getUser_bookmarksList().contains(state_id);
            commentBlock.setVisibility(View.VISIBLE);
            // Обработчик нажатий на добавл/удал. из закладок и отправку коментарием
            ListenerAction();
            if (isLiked)starButton.setImageResource(R.drawable.star);
        }
        else{
            // Он не активен - выкл. действия на странице
            commentBlock.setVisibility(View.GONE);
            starButton.setImageResource(R.drawable.star);
        }
        myRefState = FirebaseDatabase.getInstance().getReference().child(F_STATE).child(String.valueOf(state_id));
    }

    private List<CommentDTO> CommentList() {
        dataComment = new ArrayList<>();
        DatabaseReference myRefComment = FirebaseDatabase.getInstance().getReference().child(F_STATE).child(String.valueOf(state_id)).child(F_STATE_COMMENTS);
        Query query = myRefComment.orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RecyclerView viewR = (RecyclerView) getActivity().findViewById(R.id.recycleViewComment);
                TextView empty = (TextView) getActivity().findViewById(R.id.comment_null);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot comment : dataSnapshot.getChildren()) {
                        String id = String.valueOf(comment.getKey());
                        Integer image = Integer.valueOf(String.valueOf(comment.child(F_STATE_COMMENT_IMAGE).getValue()));
                        String date = String.valueOf(comment.child(F_STATE_COMMENT_DATE).getValue());
                        String author = String.valueOf(comment.child(F_STATE_COMMENT_AUTHOR).getValue());
                        String text = String.valueOf(comment.child(F_STATE_COMMENT_TEXT).getValue());
                        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm  dd.MM.yy");
                        String time = sfd.format(new Date(Long.valueOf(date)));
                        dataComment.add(new CommentDTO(id, image, time, author, text));
                    }
                    if (viewR != null && empty != null){
                        viewR.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);}
                    Collections.reverse(dataComment);
                }
                else {
                    if (viewR != null && empty != null){
                        empty.setVisibility(View.VISIBLE);
                        viewR.setVisibility(View.GONE);}
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return  dataComment;
    }

    // Отправка коментария
    private void enterComment(){
        if (commentField.getText().length() > 0) {
            // Отправка на сервер (id статьи , имя пользователя, текст коммента, дата)
            Toast.makeText(getActivity(), "Отправка комментария!", Toast.LENGTH_SHORT).show();
            ((MainActivity)getActivity()).hideKeyboard();
            DatabaseReference myRefComment = FirebaseDatabase.getInstance().getReference().child(F_STATE).child(String.valueOf(state_id)).child(F_STATE_COMMENTS).child(String.valueOf(mAuth.getCurrentUser().getUid()));
            myRefComment.child(F_STATE_COMMENT_AUTHOR).setValue(user.getUser_login());
            myRefComment.child(F_STATE_COMMENT_DATE).setValue(ServerValue.TIMESTAMP);
            myRefComment.child(F_STATE_COMMENT_IMAGE).setValue(String.valueOf(user.getUser_image()));
            myRefComment.child(F_STATE_COMMENT_TEXT).setValue(String.valueOf(commentField.getText()));
            commentField.setText(null);
        }
        commentField.clearFocus();
    }

    private void ListenerAction() {
        starButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAuth.getCurrentUser() != null && isTime){
                            isTime = false;
                            isLiked = !isLiked;
                            // Изменение состояния и уст. переменной для задержки
                            if (isLiked) {
                                if (!user.getUser_bookmarksList().contains(state_id)){
                                    // Добавляем данную статью в избранное если ее еще там нет
                                    user.getUser_bookmarksList().add(state_id);
                                    user.setUser_bookmarksList(user.getUser_bookmarksList());
                                    // Изменияем рейтинг статьи ++
                                    myRefState.child(F_S_RATING).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String value = (String) dataSnapshot.getValue();
                                            int a = Integer.valueOf(value);a++;
                                            dataSnapshot.getRef().setValue(String.valueOf(a));
                                            rating.setText(String.valueOf(a));
                                            isTime = true;
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.d("Error", databaseError.toString());
                                        }
                                    });
                                }
                                starButton.setImageResource(R.drawable.star);
                            }
                            if (!isLiked) {
                                if (user.getUser_bookmarksList().contains(state_id)){
                                    // Удаляем данную статью из избранного если она там есть
                                    (user.getUser_bookmarksList()).remove((user.getUser_bookmarksList().indexOf(state_id)));
                                    user.setUser_bookmarksList(user.getUser_bookmarksList());
                                    // Изменияем рейтинг статьи --
                                    myRefState.child(F_S_RATING).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String value = (String) dataSnapshot.getValue();
                                            int a = Integer.valueOf(value);a--;
                                            dataSnapshot.getRef().setValue(String.valueOf(a));
                                            rating.setText(String.valueOf(a));
                                            isTime = true;
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.d("Error", databaseError.toString());
                                        }
                                    });
                                }
                                starButton.setImageResource(R.drawable.star_outline);
                            }
                        }
                    }}
        );
        sendComment.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enterComment();
                    }}
        );
        commentField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    ((MainActivity)getActivity()).hideKeyboard();
                    enterComment();
                    return true;
                }
                return false;
            }
        });
    }
}
