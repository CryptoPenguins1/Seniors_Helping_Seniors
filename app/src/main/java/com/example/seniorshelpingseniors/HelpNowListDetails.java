package com.example.seniorshelpingseniors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
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

    TextView textViewitemName, textViewbrand;
    Button markAsComplete, buttonUpdateItem, buttonDeleteItem;
    String itemName, brand;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_now_list_details);
        markAsComplete = findViewById(R.id.markascomplete);

        Intent intent = getIntent();
        itemName = intent.getStringExtra("itemName");
        brand = intent.getStringExtra("brand");

        textViewitemName = (TextView) findViewById(R.id.tv_item_name);
        textViewbrand = (TextView) findViewById(R.id.tv_brand);

        textViewitemName.setText(itemName);
        textViewbrand.setText(brand);

        markAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItems();
            }
        });
    }

    //Copy Data

    private void deleteItems() {

        Intent intent = getIntent();
        final ProgressDialog loading = ProgressDialog.show(this,"Removing","Please wait");
        itemName = intent.getStringExtra("itemName");

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
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","deleteItems");
                parmas.put("itemName",itemName);

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