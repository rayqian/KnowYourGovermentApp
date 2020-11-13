package com.example.myknowyourgovermentapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class OfficialActivity extends AppCompatActivity {
    private TextView officeField;
    private TextView nameField;
    private TextView partyField;
    private ImageButton imageButton;

    private TextView address_info;
    private TextView phone_info;
    private TextView email_info;
    private TextView web_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        officeField = findViewById(R.id.officeField);
        nameField = findViewById(R.id.nameField);
        partyField = findViewById(R.id.partyField);
        imageButton = findViewById(R.id.imageButton);
        address_info = findViewById(R.id.address_info);
        phone_info = findViewById(R.id.phone_info);
        email_info = findViewById(R.id.email_info);
        web_info = findViewById(R.id.web_info);
    }


}
