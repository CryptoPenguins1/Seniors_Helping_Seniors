package com.example.seniorshelpingseniors;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HelpRequestForm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView dateDisplay;
    Button timeButton;
    TextView timeDisplay;
    TextView displayName;
    TextView addressLocation;
    TextView titleDisplay;
    TextView descriptionDisplay;
    TextView phoneDisplay;
    int hour, minute;
    GoogleSignInClient mGoogleSignInClient;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_request_form);
        dateDisplay = findViewById(R.id.fielddate);
        timeButton = findViewById(R.id.buttontime);
        timeDisplay = findViewById(R.id.timedisplay);
        titleDisplay = findViewById(R.id.fieldtitle);
        descriptionDisplay = findViewById(R.id.fielddescription);
        dateDisplay = findViewById(R.id.fielddate);
        phoneDisplay = findViewById(R.id.fieldphone);

        //Get Address info
        addressLocation = findViewById(R.id.fielduseraddress);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(HelpRequestForm.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(HelpRequestForm.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        //Google Information
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            //Get Name
            displayName = findViewById(R.id.fieldname);
            displayName.setText(acct.getDisplayName());
            //Get Email
            String emailString;
            emailString = String.valueOf(findViewById(R.id.emailaddress));
        }

        //On click listener function for calendar function
        findViewById(R.id.fieldselectdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        //Submit Button
        Button submit = (Button) findViewById(R.id.buttonsubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Run Checks as well
                addItemToSheet();
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

    //Show calendar
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
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

    //Set Date Function
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        String date = month + "/" + dayOfMonth + "/" + year;
        dateDisplay.setText(date);
    }

    //Set Time Function
    public void selectTime(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String amPm = "AM";
                String hourString;
                String minuteString;

                if (selectedHour > 12) {
                    selectedHour -= 12;
                    amPm = "PM";
                } else if (selectedHour == 0) {
                    selectedHour += 12;
                    amPm = "AM";
                } else if (selectedHour == 12)
                    amPm = "PM";
                else
                    amPm = "AM";


                if (selectedMinute < 10) {
                    minuteString = "0" + String.valueOf(selectedMinute);
                } else {
                    minuteString = String.valueOf(selectedMinute);
                }
                hour = selectedHour;
                minute = selectedMinute;

                hourString = String.valueOf(selectedHour);
                String timeString = new StringBuilder().append(hourString).append(":").append(minuteString).append(" ").append(amPm).toString();
                timeDisplay.setText(timeString);
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, false);
        timePickerDialog.setTitle("Select Time:");
        timePickerDialog.show();
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
                        Geocoder geocoder = new Geocoder(HelpRequestForm.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        addressLocation.setText(Html.fromHtml(addresses.get(0).getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void addItemToSheet() {

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }

        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        final String userName = displayName.getText().toString().trim();
        final String emailAddress = acct.getEmail();
        final String userAddress = addressLocation.getText().toString();
        final String userPhone = phoneDisplay.getText().toString();
        final String jobTitle = titleDisplay.getText().toString();
        final String jobDescription = descriptionDisplay.getText().toString();
        final String jobDate = "@" + dateDisplay.getText().toString();
        final String jobTime = "@" + timeDisplay.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwJZqqvpdFKoEKQXGeKgz2oSWfyWnsI17y7A4D3W55aS_MedtjS/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(HelpRequestForm.this,"Help Request Has Been Submitted",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),HelpScreen.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //Assigns Parameters for Javascript Backend and Passing in Variables for each Parameter
                parmas.put("action","addItem");
                parmas.put("userName",userName);
                parmas.put("emailAddress",emailAddress);
                parmas.put("userAddress",userAddress);
                parmas.put("userPhone",userPhone);
                parmas.put("jobTitle",jobTitle);
                parmas.put("jobDescription",jobDescription);
                parmas.put("jobDate",jobDate);
                parmas.put("jobTime",jobTime);

                return parmas;
            }
        };
        int socketTimeOut = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}