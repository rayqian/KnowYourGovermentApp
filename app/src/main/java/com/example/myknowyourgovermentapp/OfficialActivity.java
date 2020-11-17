package com.example.myknowyourgovermentapp;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private TextView location2;

    private TextView officeField;
    private TextView nameField;
    private TextView partyField;
    private TextView address_info;
    private TextView phone_info;
    private TextView email_info;
    private TextView web_info;

    private ImageButton imagePhoto;
    private ImageButton indicatorButton;

    private ImageButton facebook_button;
    private ImageButton twitter_button;
    private ImageButton youtube_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        Intent intent = getIntent();
        if(intent.hasExtra(Official.class.getName())){
            final Official o = (Official) intent.getSerializableExtra(Official.class.getName());
            if (o == null)
                return;

            location2 = findViewById(R.id.location2);
            location2.setText(MainActivity.loc);

            officeField = findViewById(R.id.officeField);
            officeField.setText(o.getOffice());
            nameField = findViewById(R.id.nameField);
            nameField.setText(o.getName());
            partyField = findViewById(R.id.partyField);
            partyField.setText(o.getParty());
            address_info = findViewById(R.id.address_info);
            address_info.setText(o.getOffice_address());
            phone_info = findViewById(R.id.phone_info);
            phone_info.setText(o.getPhone_number());
            email_info = findViewById(R.id.email_info);
            email_info.setText(o.getEmail());
            web_info = findViewById(R.id.web_info);
            web_info.setText(o.getWebsite());

            imagePhoto = findViewById(R.id.imagePhoto);
            loadRemoteImage(o.getPhoto_url(), imagePhoto);

            indicatorButton = (ImageButton) findViewById(R.id.indicatorButton);
            scrollView = findViewById(R.id.sv);

            if(o.getParty().equals("Republican Party")){
                indicatorButton.setImageResource(R.drawable.rep_logo);
                scrollView.setBackgroundColor(-65536);
            }

            else if(o.getParty().equals("Democratic Party")){
                indicatorButton.setImageResource(R.drawable.dem_logo);
                scrollView.setBackgroundColor(-16776961);
            }

            facebook_button = findViewById(R.id.facebook_button);
            twitter_button = findViewById(R.id.twitter_button);
            youtube_button = findViewById(R.id.youtube_button);
        }


    }

    private void loadRemoteImage(final String imageURL, ImageButton image) {
        Picasso.get().load(imageURL)
                .error(R.drawable.missing)
                .placeholder(R.drawable.placeholder)
                .into(image);

    }


}
