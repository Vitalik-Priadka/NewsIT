package com.priadka.newsit_project.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;

import static android.widget.Toast.makeText;
import static com.priadka.newsit_project.Constant.F_BOOKMARK;
import static com.priadka.newsit_project.Constant.F_EMAIL;
import static com.priadka.newsit_project.Constant.F_IMAGE;
import static com.priadka.newsit_project.Constant.F_LOGIN;
import static com.priadka.newsit_project.Constant.F_USER;
import static com.priadka.newsit_project.MainActivity.getResId;

// Класс "с логикой" фрагмента регистрации
public class RegisterFragment extends Fragment {

    private EditText loginField, emailField, passwordField, passwordFieldTwo;
    private ImageView avatarImage;
    private Button registerButton;
    private ProgressDialog progressDialog;
    private Integer numberImage = 1;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstaneState){
        return inflater.inflate(Constant.REG, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // При создании фрагмента инициализация переменных
        avatarImage  = (ImageView) getView().findViewById(R.id.user_avatar_reg);
        loginField = (EditText)  getView().findViewById(R.id.reg_login);
        emailField = (EditText)  getView().findViewById(R.id.reg_email);
        passwordField = (EditText)  getView().findViewById(R.id.reg_password);
        passwordFieldTwo = (EditText)  getView().findViewById(R.id.reg_password_two);
        registerButton = (Button) getView().findViewById(R.id.button_register);
        progressDialog = new ProgressDialog(getContext());
        // Уст. обработчик событий
        ListenerAction();
    }

    private void ListenerAction() {
        registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)getActivity()).hideKeyboard();
                        registerUser();
                    }
                }
        );
        passwordFieldTwo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    ((MainActivity)getActivity()).hideKeyboard();
                    registerUser();
                    return true;
                }
                return false;
            }
        });
        avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageAvatar();
            }
        });
    }
    // Смена аватара
    public void setImageAvatar(){
        ImageView avatarImage  = (ImageView) getView().findViewById(R.id.user_avatar_reg);
        if(numberImage < 0 || numberImage >= 12){
            numberImage = 1;
        }
        else numberImage++;
        String currentAvatarS = "avatar_" + numberImage;
        avatarImage.setImageResource(getResId(currentAvatarS, R.drawable.class));
    }
    // Запросна регистрацию
    private void registerUser() {
        progressDialog.setMessage(getString(R.string.reg_waiting));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String login = loginField.getText().toString(), password = passwordField.getText().toString(),
                email = emailField.getText().toString(), passwordTwo = passwordFieldTwo.getText().toString();
        // Различные проверки email, login, password
        if (login.length() >= 5){
            if(email.length() >= 8){
                if(password.length() >= 8){
                    if (password.equals(passwordTwo)){
                        // Регистрация пользователя
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Отправка данных на сервер
                                    addUserDatabase(login, email, password, String.valueOf(numberImage));
                                    if (progressDialog.isShowing())progressDialog.dismiss();
                                } else {
                                    // Поиск причины неудачи
                                    String stateOfWrong = getString(R.string.log_error_connection);
                                    try {
                                        throw task.getException();
                                    } catch(FirebaseAuthException e) {
                                        switch (e.getErrorCode()){
                                            case "ERROR_INVALID_EMAIL":stateOfWrong = getString(R.string.reg_error_invalid_email);break;
                                            case "ERROR_EMAIL_ALREADY_IN_USE":stateOfWrong = getString(R.string.reg_error_email);break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    makeText(getActivity(),getString(R.string.reg_error) + " " + stateOfWrong, Toast.LENGTH_SHORT).show();
                                    if (progressDialog.isShowing())progressDialog.dismiss();
                                }
                            }});
                    }
                    else{
                        passwordField.setText("");passwordFieldTwo.setText("");
                        makeText(getContext(),getString(R.string.reg_password_error_2), Toast.LENGTH_SHORT).show();
                    }}
                else makeText(getContext(),getString(R.string.reg_password_error_1), Toast.LENGTH_SHORT).show();}
            else makeText(getContext(),getString(R.string.reg_email_error), Toast.LENGTH_SHORT).show();}
        else makeText(getContext(),getString(R.string.reg_login_error), Toast.LENGTH_SHORT).show();
        if (progressDialog.isShowing())progressDialog.dismiss();
    }

    // Общий шаблон добавления пользователя в БД
    private void addUserDatabase(final String login, final String email, final String password, final String image) {
        FirebaseUser userFire = mAuth.getCurrentUser();
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference().child(F_USER).child(userFire.getUid());
        user.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                mutableData.child(F_LOGIN).setValue(login);
                mutableData.child(F_EMAIL).setValue(email);
                mutableData.child(F_BOOKMARK).setValue("");
                mutableData.child(F_IMAGE).setValue(image);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean success, DataSnapshot dataSnapshot) {
                if (success){
                    // Изменение локальных переменных для взода в новый акк и сам вход
                    ((MainActivity)getActivity()).setLocalEmail(email);
                    ((MainActivity)getActivity()).setLocalPassword(password);
                    ((MainActivity)getActivity()).setSavePassword(true);
                    ((MainActivity)getActivity()).loginUser(password);
                    ((MainActivity)getActivity()).doPreferences(true);
                }
                else makeText(getContext(),"Ошибка регистрации!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
