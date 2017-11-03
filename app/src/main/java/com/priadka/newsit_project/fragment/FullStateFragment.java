package com.priadka.newsit_project.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.DTO.UserDTO;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;
import com.priadka.newsit_project.myServer;

public class FullStateFragment extends Fragment {
    private View view;
    private ImageView image;
    private ImageButton starButton, sendComment;
    private TextView title, text, date, rating;
    private EditText commentField;
    private LinearLayout commentBlock;
    private UserDTO user;
    private FirebaseAuth mAuth;

    private String state_title, state_text, state_date;
    private int state_id, state_image,state_rating ;
    private boolean isLiked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        view = inflater.inflate(Constant.FULL_STATE, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            state_id = bundle.getInt("state_id",0);
            if(state_id == 0){
                NewsFragment newsFragment = new NewsFragment();
                ((MainActivity)getActivity()).FragmentDo(newsFragment);
            }
            state_title = bundle.getString("state_title");
            state_text = bundle.getString("state_text");
            state_image = bundle.getInt("state_image",1);
            state_date = bundle.getString("state_date");
            state_rating = bundle.getInt("state_rating",0);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        user = myServer.getUser();  mAuth = FirebaseAuth.getInstance();
        image = (ImageView) getActivity().findViewById(R.id.state_image);
        title = (TextView) getActivity().findViewById(R.id.state_header);
        title = (TextView) getActivity().findViewById(R.id.state_header);
        text = (TextView) getActivity().findViewById(R.id.state_text);
        date = (TextView) getActivity().findViewById(R.id.state_date);
        rating = (TextView) getActivity().findViewById(R.id.state_rating);
        starButton  = (ImageButton) getActivity().findViewById(R.id.state_star_like);
        commentBlock = (LinearLayout) getActivity().findViewById(R.id.commentBlock);

        title.setText(state_title);
        text.setText(state_text+ " " + getString(R.string.test_text));
        image.setImageResource( MainActivity.getResId("avatar_" + state_image, R.drawable.class) );
        date.setText(state_date);
        rating.setText(String.valueOf(state_rating));

        if(mAuth.getCurrentUser() != null){
            commentField = (EditText) getActivity().findViewById(R.id.state_comment_field);
            sendComment = (ImageButton) getActivity().findViewById(R.id.sendComment);
            isLiked = user.getUser_bookmarksList().contains(state_id);
            commentBlock.setVisibility(View.VISIBLE);
            ListenerAction();
            if (isLiked)starButton.setImageResource(R.drawable.star);
        }
        else{
            commentBlock.setVisibility(View.GONE);
            starButton.setImageResource(R.drawable.star);
        }
    }

    private void enterComment(){
        if (commentField.getText().length() > 0) {
            // Отправка на сервер (id статьи , имя пользователя, текст коммента)
            Toast.makeText(getActivity(), "Отправка коммента", Toast.LENGTH_SHORT).show();
            commentField.setText(null);
            ((MainActivity)getActivity()).hideKeyboard();
            commentField.clearFocus();
        }
    }

    private void ListenerAction() {
        starButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAuth.getCurrentUser() != null){
                            isLiked = !isLiked;
                            if (isLiked) {
                                if (!user.getUser_bookmarksList().contains(state_id)){
                                    (user.getUser_bookmarksList()).add(state_id);
                                    state_rating++;
                                }
                                starButton.setImageResource(R.drawable.star);
                            }
                            if (!isLiked) {
                                if (user.getUser_bookmarksList().contains(state_id)){
                                    (user.getUser_bookmarksList()).remove((user.getUser_bookmarksList().indexOf(state_id)));
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
