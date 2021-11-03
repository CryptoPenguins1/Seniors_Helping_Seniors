package com.example.seniorshelpingseniors;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfileScreen extends AppCompatActivity {

    //Initialize Address Variables
    Button getAddress;
    TextView addressLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleSignInClient mGoogleSignInClient;
    //End Address Initialize

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        //Get Address info
        getAddress = findViewById(R.id.getaddress);
        addressLocation = findViewById(R.id.useraddress);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(ProfileScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(ProfileScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        getAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressLocation.setVisibility(View.VISIBLE);

                if (ActivityCompat.checkSelfPermission(ProfileScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(ProfileScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
        //End Address Function

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            //Get Profile Picture
            ImageView userImage = findViewById(R.id.profilepicture);
            if (acct.getPhotoUrl() != null) {
                Glide.with(this).load(personPhoto).into(userImage);
            } else {

            }
            //Get Name
            TextView userName = findViewById(R.id.username);
            userName.setText(acct.getDisplayName());
            //Get Email
            TextView emailAddress = findViewById(R.id.emailaddress);
            emailAddress.setText(acct.getEmail());
        }

        //SignOut Button
        Button signOutButton = findViewById(R.id.signout);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
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
        profile.setColorFilter(Color.BLUE);
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
    //Google Sign Out Function
    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(ProfileScreen.this, MainActivity.class));
                        Toast.makeText(ProfileScreen.this, "You have successfully signed out", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //Address Private Function
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(ProfileScreen.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        addressLocation.setText(Html.fromHtml(addresses.get(0).getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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