package com.priadka.newsit_project;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends MainActivity {
    private EditText loginField;
    private EditText passwordField;
    private CheckBox saveOrNotBox;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Constant.LOG);
        initToolbar();
        initNavigationView();
        ShortToolbar(R.string.menu_item_log_in);
        ListenerAction();
    }
    private void checkLogin(){
        loginField = (EditText) findViewById(R.id.login_field);
        String login = loginField.getText().toString();
        passwordField = (EditText) findViewById(R.id.password_field);
        String password = passwordField.getText().toString();
        saveOrNotBox = (CheckBox) findViewById(R.id.checkBox_save);
        Boolean save = saveOrNotBox.isChecked();
        if (save){
            //Сохрание в кеш

        }

        if(login.length() >= 5 && password.length() >= 5){
            loginField.setText(null); passwordField.setText(null);
            loginUser(login,password);
        }
    }
    private  void ListenerAction() {
        passwordField = (EditText) findViewById(R.id.password_field);
        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard();
                        checkLogin();
                    }
                }
        );
        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    hideKeyboard();
                    checkLogin();
                    return true;
                }
                return false;
            }
        });
    }
}