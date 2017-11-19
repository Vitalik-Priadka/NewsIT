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
import android.widget.Toast;

import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;

import static android.widget.Toast.makeText;

public class LoginFragment extends Fragment {

    private EditText passwordField, loginField;
    private Button loginButton;
    private CheckBox saveOrNotBox;

    private String password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstaneState){
        return inflater.inflate(Constant.LOG, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // При создании фрагмента инициализация переменных
        passwordField = (EditText)  getView().findViewById(R.id.password_field);
        loginField = (EditText)  getView().findViewById(R.id.login_field);
        loginButton = (Button) getView().findViewById(R.id.button_login);
        saveOrNotBox = (CheckBox) getView().findViewById(R.id.checkBox_save);
        // Установка email и password если они есть сохраненные
        loginField.setText(((MainActivity)getActivity()).getLocalEmail());
        if (((MainActivity)getActivity()).getSavePassword()) passwordField.setText(((MainActivity)getActivity()).getLocalPassword());
        saveOrNotBox.setChecked(((MainActivity)getActivity()).getSavePassword());
        // Обработчик событий
        ListenerAction();
    }

    private void ListenerAction() {
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
        // Проверка, сохранение и вход в акк
        ((MainActivity)getActivity()).setLocalEmail(loginField.getText().toString());
        password = passwordField.getText().toString();
        Boolean save = saveOrNotBox.isChecked();
        if (!save){
            ((MainActivity)getActivity()).setSavePassword(false);
            passwordField.setText("");
        }
        else {((MainActivity)getActivity()).setSavePassword(true);}
        if(((MainActivity)getActivity()).getLocalEmail().length() >= 8){
            if (password.length() >= 8){
                ((MainActivity)getActivity()).setLocalPassword(passwordField.getText().toString());
                ((MainActivity)getActivity()).loginUser(password);
            } else makeText(getContext(),getString(R.string.reg_password_error_1), Toast.LENGTH_SHORT).show();
        }else makeText(getContext(),getString(R.string.reg_email_error), Toast.LENGTH_SHORT).show();
    }
}