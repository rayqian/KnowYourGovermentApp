package com.example.myknowyourgovermentapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {
    private TextView location3;
    private TextView officeNameField;
    private TextView nameField;
    private ImageView iv;
    private ImageView party_logo;
    private View photo_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        location3 = findViewById(R.id.location3);
        location3.setText(MainActivity.loc);

        officeNameField = findViewById(R.id.office_name_field);
        nameField = findViewById(R.id.name_field);
        iv = findViewById(R.id.photo);
        party_logo = findViewById(R.id.party_logo);
        photo_layout = findViewById(R.id.photo_layout);

        Intent intent = getIntent();
        if(intent.hasExtra(Official.class.getName())){
            final Official o = (Official) intent.getSerializableExtra(Official.class.getName());
            officeNameField.setText(o.getOffice());
            nameField.setText(o.getName());
            loadRemoteImage(o.getPhoto_url(), iv);


            if(o.getParty().equals("Republican Party") || o.getParty().equals("Republican")){
                party_logo.setImageResource(R.drawable.rep_logo);
                photo_layout.setBackgroundColor(-65536);
            }
            else if(o.getParty().equals("Democratic Party") || o.getParty().equals("Democratic")){
                party_logo.setImageResource(R.drawable.dem_logo);
                photo_layout.setBackgroundColor(-16776961);
            }
            else{
                party_logo.setVisibility(View.GONE);
                photo_layout.setBackgroundColor(-16777216);
            }
        }

    }

    @Override
    public void onBackPressed(){
        finish();
    }

    //loading pic from remote url using Picasso
    private void loadRemoteImage(final String imageURL, ImageView image) {
            Picasso.get().load(imageURL)
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
    }
}
