package com.priadka.newsit_project.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;

public class LoginFragment extends Fragment {

    private EditText passwordField, loginField;
    private Button loginButton;
    private CheckBox saveOrNotBox;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstaneState){
        return inflater.inflate(Constant.LOG, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        passwordField = (EditText)  getView().findViewById(R.id.password_field);
        loginField = (EditText)  getView().findViewById(R.id.login_field);
        loginButton = (Button) getView().findViewById(R.id.button_login);
        saveOrNotBox = (CheckBox) getView().findViewById(R.id.checkBox_save);
        ListenerAction();
    }

    private  void ListenerAction() {
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).hideKeyboard();
                        checkLogin();
                    }
                }
        );
        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    ((MainActivity)getActivity()).hideKeyboard();
                    checkLogin();
                    return true;
                }
                return false;
            }
        });
    }

    private void checkLogin(){
        String login = loginField.getText().toString();
        String password = passwordField.getText().toString();
        Boolean save = saveOrNotBox.isChecked();
        if (save){
            //Сохрание в кеш
        }
        if(login.length() >= 5 && password.length() >= 5){
            loginField.setText(null); passwordField.setText(null);
            ((MainActivity)getActivity()).loginUser(login,password);
        }
    }
}