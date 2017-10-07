package com.priadka.newsit_project;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingActivity extends MainActivity {
    private Spinner spinnerLanguage;
    private Spinner spinnerTheme;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Constant.SETTING);
        initToolbar();
        initNavigationView();
        ShortToolbar(R.string.menu_item_settings);
        initSpinnerLanguage();   initSpinnerTheme();
    }

    public void initSpinnerLanguage(){
        spinnerLanguage = (Spinner) findViewById(R.id.spinner_language);
        spinnerLanguage.getBackground().setColorFilter(getResources().getColor(R.color.textColorDark), PorterDuff.Mode.SRC_ATOP);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                Toast.makeText(SettingActivity.this, "Value is " + selectedLanguage + "! Position:"+ position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initSpinnerTheme(){
        spinnerTheme = (Spinner) findViewById(R.id.spinner_theme);
        spinnerTheme.getBackground().setColorFilter(getResources().getColor(R.color.textColorDark), PorterDuff.Mode.SRC_ATOP);
        spinnerTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTheme = parent.getItemAtPosition(position).toString();
                Toast.makeText(SettingActivity.this, "Value is " + selectedTheme + "! Position:"+ position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void submitToEmail(View view) {
        // Отправка на email
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: it.news@dev.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.setting_info_email_header));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.setting_info_email_text));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            CharSequence text = getString(R.string.error_text);
            Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
        return;
    }

}
