package com.example.myknowyourgovermentapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.text.util.Linkify;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private TextView location2;

    private TextView officeField;
    private TextView nameField;
    private TextView partyField;
    private String party;
    private TextView address_info;
    private TextView address_title;
    private TextView phone_info;
    private TextView phone_title;
    private TextView email_info;
    private TextView email_title;
    private TextView web_info;
    private TextView website_title;

    private ImageButton imagePhoto;
    private ImageButton indicatorButton;

    private ImageButton facebook_button;
    private String facebook;
    private ImageButton twitter_button;
    private String twitter;
    private ImageButton youtube_button;
    private String youtube;

    private Official of;//used for onClick photo activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        Intent intent = getIntent();
        if(intent.hasExtra(Official.class.getName())){
            final Official o = (Official) intent.getSerializableExtra(Official.class.getName());
            of = o;//used for another activity
            if (o == null)
                return;
            //set location widget
            location2 = findViewById(R.id.location2);
            location2.setText(MainActivity.loc);
            //set office
            officeField = findViewById(R.id.officeField);
            officeField.setText(o.getOffice());
            //set officer's name
            nameField = findViewById(R.id.nameField);
            nameField.setText(o.getName());
            //set party name
            partyField = findViewById(R.id.partyField);
            party = o.getParty();
            partyField.setText(o.getParty());
            //set address, make it invisible if null value received
            address_info = findViewById(R.id.address_info);
            address_title = findViewById(R.id.address_title);
            if(o.getOffice_address() == null){
                address_info.setVisibility(View.GONE);
                address_title.setVisibility(View.GONE);
            }
            else{
                address_info.setText(o.getOffice_address());
                Linkify.addLinks(address_info, Linkify.ALL);
            }
            //set phone number, make it invisible if null value received
            phone_info = findViewById(R.id.phone_info);
            phone_title = findViewById(R.id.phone_title);
            if(o.getPhone_number() == null){
                phone_info.setVisibility(View.GONE);
                phone_title.setVisibility(View.GONE);
            }
            else{
                phone_info.setText(o.getPhone_number());
                Linkify.addLinks(phone_info, Linkify.ALL);
            }
            //set email address, make it invisible if null value received
            email_info = findViewById(R.id.email_info);
            email_title = findViewById(R.id.email_title);
            if(o.getEmail() == null){
                email_info.setVisibility(View.GONE);
                email_title.setVisibility(View.GONE);
            }
            else{
                email_info.setText(o.getEmail());
                Linkify.addLinks(email_info, Linkify.ALL);
            }
            //set website, make it invisible if null value received
            web_info = findViewById(R.id.web_info);
            website_title = findViewById(R.id.website_title);
            if(o.getWebsite() == null){
                web_info.setVisibility(View.GONE);
                website_title.setVisibility(View.GONE);
            }
            else{
                web_info.setText(o.getWebsite());
                Linkify.addLinks(web_info, Linkify.ALL);
            }
            //set the image photo, calling Picasso from function
            imagePhoto = findViewById(R.id.imagePhoto);
            loadRemoteImage(o.getPhoto_url(), imagePhoto);
            //set the party logo associated with background color
            indicatorButton = (ImageButton) findViewById(R.id.indicatorButton);
            scrollView = findViewById(R.id.sv);
            if(o.getParty().equals("Republican Party") || o.getParty().equals("Republican")){
                indicatorButton.setImageResource(R.drawable.rep_logo);
                scrollView.setBackgroundColor(-65536);
            }
            else if(o.getParty().equals("Democratic Party") || o.getParty().equals("Democratic")){
                indicatorButton.setImageResource(R.drawable.dem_logo);
                scrollView.setBackgroundColor(-16776961);
            }
            else{
                indicatorButton.setVisibility(View.GONE);
                scrollView.setBackgroundColor(-16777216);
            }
            //set the facebook button, make it invisible if null value received
            facebook_button = findViewById(R.id.facebook_button);
            if(o.getFacebook() == null){
                facebook_button.setVisibility(View.GONE);
            }
            else{
                facebook = o.getFacebook();
            }
            //set the twitter button, make it invisible if null value received
            twitter_button = findViewById(R.id.twitter_button);
            if(o.getTwitter() == null){
                twitter_button.setVisibility(View.GONE);
            }
            else{
                twitter = o.getTwitter();
            }
            //set the youtube button, make it invisible if null value received
            youtube_button = findViewById(R.id.youtube_button);
            if(o.getYoutube() == null){
                youtube_button.setVisibility(View.GONE);
            }
            else{
                youtube = o.getYoutube();
            }
        }
    }
    //loading pic from remote url using Picasso
    private void loadRemoteImage(final String imageURL, ImageButton image) {
        if(imageURL == null){
            Picasso.get().load(imageURL)
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.missing)
                    .into(image);
        }
        else{
            Picasso.get().load(imageURL)
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
        }

    }
    //onClick method for facebook
    public void clickFacebook(View v) {
        String fbName = facebook;
        String FACEBOOK_URL = "https://www.facebook.com/" + fbName;
        Intent intent;
        String urlToUse;
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);

            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + fbName;
            }
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
        }
        startActivity(intent);
    }
    //onClick method for Twitter
    public void clickTwitter(View v) {
        String user = twitter;
        String twitterAppUrl = "twitter://user?screen_name=" + user;
        String twitterWebUrl = "https://twitter.com/" + user;
        Intent intent;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }
        startActivity(intent);
    }
    //onClick method for youtube
    public void clickYouTube(View v) {
        String user = youtube;
        String youtubeAppUrl = "vnd.youtube/" + user;
        String youtubeWebUrl = "https://www.youtube.com/" + user;

        Intent intent;
        try {
            getPackageManager().getPackageInfo("com.google.android.youtube", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeAppUrl));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeWebUrl));
        }
        startActivity(intent);
    }


    //onClick method for party logo
    public void clickLogo(View v) {

        String blueUrl = "https://democrats.org/";
        String redUrl = "https://www.gop.com";

        Intent intent = null;
        if(party.equals("Republican Party") || party.equals("Republican") ){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(redUrl));
        }
        else if(party.equals("Democratic Party") || party.equals("Democratic")){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(blueUrl));
        }
        if(intent != null){
            startActivity(intent);
        }
    }

    //onClick method for photo
    public void clickPhoto(View v) {
        if(of.getPhoto_url() != null){
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtra(Official.class.getName(), of);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "no photo activity if photo is null", Toast.LENGTH_SHORT ).show();
        }
    }

}
