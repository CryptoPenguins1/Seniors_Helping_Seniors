package com.example.seniorshelpingseniors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class HelpNowListDetails extends AppCompatActivity {

    TextView textUserName, textEmail, textUserAddress, textUserPhone, textJobTitle, textJobDescription, textJobDate, textJobTime;
    Button markAsComplete, getDirections;
    String userName, emailAddress, userAddress, userPhone, jobTitle, jobDescription, jobDate, jobTime, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_now_list_details);
        markAsComplete = findViewById(R.id.markascomplete);
        getDirections = findViewById(R.id.getdirections);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        emailAddress = intent.getStringExtra("emailAddress");
        userAddress = intent.getStringExtra("userAddress");
        userPhone = intent.getStringExtra("userPhone");
        jobTitle = intent.getStringExtra("jobTitle");
        jobDescription = intent.getStringExtra("jobDescription");
        jobDate = intent.getStringExtra("jobDate").replace("@", "");
        jobTime = intent.getStringExtra("jobTime").replace("@", "");
        date = intent.getStringExtra("date");

        textUserName = (TextView) findViewById(R.id.fieldusername);
        textEmail = (TextView) findViewById(R.id.fieldemail);
        textUserAddress = (TextView) findViewById(R.id.fielduseraddress);
        textUserPhone = (TextView) findViewById(R.id.fielduserphone);
        textJobTitle = (TextView) findViewById(R.id.fieldjobtitle);
        textJobDescription = (TextView) findViewById(R.id.fieldjobdescription);
        textJobDate = (TextView) findViewById(R.id.fieldjobdate);
        textJobTime = (TextView) findViewById(R.id.fieldjobtime);

        textUserName.setText(userName);
        textEmail.setText(emailAddress);
        textUserAddress.setText(userAddress);
        textUserPhone.setText(userPhone);
        textJobTitle.setText(jobTitle);
        textJobDescription.setText(jobDescription);
        textJobDate.setText(jobDate);
        textJobTime.setText(jobTime);

        //Get Directions
        getDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapsApp();
            }
        });
        //Run Mark as Complete to Delete Data from Spreadsheet
        markAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItems();
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
        offerhelp.setColorFilter(Color.BLUE);
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

    //Delete from Spreadsheet Function
    private void deleteItems() {

        Intent intent = getIntent();
        final ProgressDialog loading = ProgressDialog.show(this,"Marking Job As Complete","Please wait");
        //userName = intent.getStringExtra("userName");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwJZqqvpdFKoEKQXGeKgz2oSWfyWnsI17y7A4D3W55aS_MedtjS/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(HelpNowListDetails.this,"Job Has Been Marked As Complete",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),HelpNow.class);
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
                Map<String, String> params = new HashMap<>();

                //here we pass params
                params.put("action","deleteItems");
                params.put("date",date);

                return params;
            }
        };
        int socketTimeOut = 5000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    //Open Maps
    public void openMapsApp() {
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(userAddress));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
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