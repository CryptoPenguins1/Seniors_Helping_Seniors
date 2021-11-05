package com.example.seniorshelpingseniors;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SettingsScreen extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }

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
        settings.setColorFilter(Color.BLUE);
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
    public void onClickNotifications(View v) {
        switch (v.getId()) {
            case R.id.enable_tots_button:
                enableNotifications();
                break;
        }
    }
    public void onClickSupport(View v) {
        switch (v.getId()) {
            case R.id.support_button:
                showSupport();
                break;
        }
    }

    public void onClickResetApp(View v) {
        switch (v.getId()) {
            // ...
            case R.id.reset_button:
                resetApplication();
                break;
            // ...
        }
    }

    private void resetApplication()
    {
        android.app.AlertDialog.Builder builder
                = new android.app.AlertDialog
                .Builder(SettingsScreen.this);

        // Set the message show for the Alert time
        builder.setMessage("Are you sure you want to continue?\nYOU WILL BE SIGNED OUT\nAPP DATA WILL BE CLEARED");

        // Set Alert Title
        builder.setTitle("Alert!");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                signOut(); // Signing user out
                                getCacheDir().delete(); // Clear cache
                                finish(); // Close app
                                startActivity(getIntent()); // Restart app

                                // this basically provides animation
                                overridePendingTransition(0, 0);
                                String time = System.currentTimeMillis() + "";
                                //finish();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        android.app.AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    private void showSupport()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Seniors Helping Seniors is designed for senior citizens to request help for simple jobs that they list. Users can post jobs, as well as fulfill them which removes them from view.\nCleveland State University\nIST 465 Fall 2021\nAnthony Nelson, Samuel Mamer, Emily Madej, Nazar Loshak");
        dialog.setTitle("Support");
        dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }

    public void enableNotifications()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Notifications Enabled!");
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
    public void onClickDisableNotifications(View v) {
        switch (v.getId()) {
            case R.id.disable_tots_button2:
                disableNotifications();
                break;
        }
    }
    private void disableNotifications()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("Notifications Disabled!");
        AlertDialog alertDialog=dialog.create();
        alertDialog.show();
    }
    public void onClickLogout(View v) {
        switch (v.getId()) {
            case R.id.logout_button:
                signOut();
                break;
        }
    }
    //Google Sign Out Function
    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(SettingsScreen.this, MainActivity.class));
                        Toast.makeText(SettingsScreen.this, "You have successfully signed out", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}