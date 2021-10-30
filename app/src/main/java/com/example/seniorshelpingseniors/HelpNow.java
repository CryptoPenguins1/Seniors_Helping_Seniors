package com.example.seniorshelpingseniors;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HelpNow extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ListAdapter adapter;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_now);

        //Initalize ListView and start process for getting items so it loads immediately.
        listView = (ListView) findViewById(R.id.lv_items);
        getItems();
        listView.setOnItemClickListener(this);
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

    private void getItems() {
        loading =  ProgressDialog.show(this,"Loading","Please Wait, Fetching all Open Jobs",false,true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://script.google.com/macros/s/AKfycbwJZqqvpdFKoEKQXGeKgz2oSWfyWnsI17y7A4D3W55aS_MedtjS/exec?action=getItems",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
    private void parseItems(String jsonResponse) {

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResponse);
            JSONArray jarray = jobj.getJSONArray("items");

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject jo = jarray.getJSONObject(i);

                String userName = jo.getString("userName");
                String emailAddress = jo.getString("emailAddress");
                String userAddress = jo.getString("userAddress");
                String userPhone = jo.getString("userPhone");
                String jobTitle = jo.getString("jobTitle");
                String jobDescription = jo.getString("jobDescription");
                String jobDate = jo.getString("jobDate").replace("@", "");
                String jobTime = jo.getString("jobTime").replace("@", "");

                HashMap<String, String> item = new HashMap<>();
                item.put("userName", userName);
                item.put("emailAddress", emailAddress);
                item.put("userAddress", userAddress);
                item.put("userPhone", userPhone);
                item.put("jobTitle", jobTitle);
                item.put("jobDescription", jobDescription);
                item.put("jobDate", jobDate);
                item.put("jobTime", jobTime);

                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new SimpleAdapter(this,list,R.layout.activity_help_now_list,
                new String[]{"userName", "emailAddress", "userAddress", "userPhone", "jobTitle", "jobDescription", "jobDate", "jobTime"},new int[]{R.id.fieldusername,R.id.fieldemail,R.id.fielduseraddress,R.id.fielduserphone,R.id.fieldjobtitle,R.id.fieldjobdescription,R.id.fielddate,R.id.fieldjobtime});
        listView.setAdapter(adapter);
        loading.dismiss();
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, HelpNowListDetails.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);

        String userName = map.get("userName").toString();
        String emailAddress = map.get("emailAddress").toString();
        String userAddress = map.get("userAddress").toString();
        String userPhone = map.get("userPhone").toString();
        String jobTitle = map.get("jobTitle").toString();
        String jobDescription = map.get("jobDescription").toString();
        String jobDate = map.get("jobDate");
        String jobTime = map.get("jobTime");
        intent.putExtra("userName",userName);
        intent.putExtra("emailAddress",emailAddress);
        intent.putExtra("userAddress",userAddress);
        intent.putExtra("userPhone",userPhone);
        intent.putExtra("jobTitle",jobTitle);
        intent.putExtra("jobDescription",jobDescription);
        intent.putExtra("jobDate",jobDate);
        intent.putExtra("jobTime",jobTime);

        startActivity(intent);
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
    //Pointer Capture Override
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}