package com.example.seniorshelpingseniors;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        //Go to Request Help Screen
        ImageView requesthelp = (ImageView) findViewById(R.id.requesthelp);
        requesthelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRequestHelp();
            }
        });
        //Go to Help Now Screen
        ImageView offerhelp = (ImageView) findViewById(R.id.offerhelp);
        offerhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOfferHelp();
            }
        });
        //Go to Profile Screen
        ImageView profile = (ImageView) findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
        //Go to Settings Screen
        ImageView settings = (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

    }

    //Request Help Screen Function
    public void openRequestHelp() {
        Intent intent = new Intent(this, HelpScreen.class);
        startActivity(intent);
    }
    //Offer Help Screen Function
    public void openOfferHelp() {
        Intent intent = new Intent(this, HelpNow.class);
        startActivity(intent);
    }
    //Profile Screen Function
    public void openProfile() {
        Intent intent = new Intent(this, ProfileScreen.class);
        startActivity(intent);
    }
    //Settings Screen Function
    public void openSettings() {
        Intent intent = new Intent(this, SettingsScreen.class);
        startActivity(intent);
    }
}