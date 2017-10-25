package com.priadka.newsit_project.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;

public class FullStateFragment extends Fragment {
    private View view;
    private ImageView image;
    private ImageButton starButton;
    private TextView title, text, date, rating;
    private String state_title, state_text, state_date, state_rating;
    private int state_id, state_image;
    private boolean isLiked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        view = inflater.inflate(Constant.FULL_STATE, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            state_id = bundle.getInt("state_image",0);
            if(state_id == 0 ){
                NewsFragment newsFragment = new NewsFragment();
                ((MainActivity)getActivity()).FragmentDo(newsFragment);
            }
            state_title = bundle.getString("state_title");
            state_text = bundle.getString("state_text");
            state_image = bundle.getInt("state_image",1);
            state_date = bundle.getString("state_date");
            state_rating = bundle.getString("state_rating");
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        image = (ImageView) getActivity().findViewById(R.id.state_image);
        title = (TextView) getActivity().findViewById(R.id.state_header);
        title = (TextView) getActivity().findViewById(R.id.state_header);
        text = (TextView) getActivity().findViewById(R.id.state_text);
        date = (TextView) getActivity().findViewById(R.id.state_date);
        rating = (TextView) getActivity().findViewById(R.id.state_rating);
        starButton  = (ImageButton) getActivity().findViewById(R.id.state_star_like);

        title.setText(state_title);
        text.setText(state_text + getString(R.string.test_text));
        image.setImageResource( MainActivity.getResId("avatar_" + state_image, R.drawable.class) );
        date.setText(state_date);
        rating.setText(state_rating);
        ListenerAction();
    }

    private void ListenerAction() {
        starButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isLiked = !isLiked;
                        if (isLiked){ starButton.setImageResource(R.drawable.star); }
                        if (!isLiked){ starButton.setImageResource(R.drawable.star_outline); }
                    }}
        );
        /*
        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    ((MainActivity)getActivity()).hideKeyboard();
                    checkLogin();
                    return true;
                }
                return false;
            }
        });*/
    }
}
