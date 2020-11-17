package com.example.myknowyourgovermentapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;


public class AboutActivity extends AppCompatActivity {

    private TextView api_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //set underline for the API text
        api_field = findViewById(R.id.api_field);
        String text="Google Civic Information API";
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        api_field.setText(content);
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    //onClick method for API text
    public void clickAPI(View v) {
        String Url = "https://developers.google.com/civic-information/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        startActivity(intent);
    }
}
