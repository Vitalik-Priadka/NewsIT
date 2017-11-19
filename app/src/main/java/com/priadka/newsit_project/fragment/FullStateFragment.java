package com.priadka.newsit_project.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.DTO.UserDTO;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;

import static com.priadka.newsit_project.Constant.F_STATE;
import static com.priadka.newsit_project.Constant.F_S_IMAGE_DATABASE;
import static com.priadka.newsit_project.Constant.F_S_RATING;

public class FullStateFragment extends Fragment {
    private ImageButton starButton, sendComment;
    private TextView rating;
    private EditText commentField;

    private UserDTO user;
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
        }
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
    // Отправка коментария
    private void enterComment(){
        if (commentField.getText().length() > 0) {
            // Отправка на сервер (id статьи , имя пользователя, текст коммента, дата)
            Toast.makeText(getActivity(), "Отправка коммента", Toast.LENGTH_SHORT).show();
            commentField.setText(null);
            ((MainActivity)getActivity()).hideKeyboard();
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
