package com.example.myknowyourgovermentapp;

import android.content.Intent;
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

        Intent intent = getIntent();
        if(intent.hasExtra(Official.class.getName())){
            final Official o = (Official) intent.getSerializableExtra(Official.class.getName());
            if (o == null)
                return;

            officeField = findViewById(R.id.officeField);
            officeField.setText(o.getOffice());

            nameField = findViewById(R.id.nameField);
            nameField.setText(o.getName());

            partyField = findViewById(R.id.partyField);
            partyField.setText(o.getParty());

            imageButton = findViewById(R.id.imageButton);
            address_info = findViewById(R.id.address_info);
            phone_info = findViewById(R.id.phone_info);
            email_info = findViewById(R.id.email_info);
            web_info = findViewById(R.id.web_info);
        }


    }


}
