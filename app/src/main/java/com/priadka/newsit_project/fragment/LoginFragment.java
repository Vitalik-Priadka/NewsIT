package com.priadka.newsit_project.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.priadka.newsit_project.Constant;
import com.priadka.newsit_project.MainActivity;
import com.priadka.newsit_project.R;

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
        passwordField = (EditText)  getView().findViewById(R.id.password_field);
        loginField = (EditText)  getView().findViewById(R.id.login_field);
        loginButton = (Button) getView().findViewById(R.id.button_login);
        saveOrNotBox = (CheckBox) getView().findViewById(R.id.checkBox_save);
        loginField.setText(((MainActivity)getActivity()).getLocalEmail());
        passwordField.setText(((MainActivity)getActivity()).getLocalPassword());
        saveOrNotBox.setChecked(((MainActivity)getActivity()).getSavePassword());
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
        ((MainActivity)getActivity()).setLocalEmail(loginField.getText().toString());
        password = passwordField.getText().toString();
        Boolean save = saveOrNotBox.isChecked();
        if (!save){
            ((MainActivity)getActivity()).setSavePassword(false);
            passwordField.setText("");
        }
        else {((MainActivity)getActivity()).setSavePassword(true);}
        if(((MainActivity)getActivity()).getLocalEmail().length() >= 5){
            ((MainActivity)getActivity()).setLocalPassword(password);

            //TypedValue typedValue = new  TypedValue();
            //getContext().getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            //final  int color = typedValue.data;
            new MyAsyncTask().execute();
        }
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getString(R.string.log_waiting));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ((MainActivity)getActivity()).loginUser(password);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (progressDialog.isShowing())progressDialog.dismiss();
            FirebaseUser userFire = FirebaseAuth.getInstance().getCurrentUser();
            if(userFire != null){
                Toast.makeText(getActivity(),getString(R.string.log_success), Toast.LENGTH_SHORT).show();
                NewsFragment newsFragment = new NewsFragment();
                ((MainActivity)getActivity()).FragmentDo(newsFragment);
            }
}
    }
}