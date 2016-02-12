package com.homeautomation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.support.v7.widget.SwitchCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hacker on 11/2/16.
 */
public class HomeActivity extends Activity {

    Button logoutBtn;
    TextView status1, status2, status3,
                statusDes1, statusDes2, statusDes3;
    SwitchCompat lightSwitch;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        settings = getSharedPreferences(Utils.SHARED_PREFERENCES, 0);

        logoutBtn = (Button) findViewById(R.id.btn_logout);
        status1 = (TextView) findViewById(R.id.status_1);
        status2 = (TextView) findViewById(R.id.status_2);
        status3 = (TextView) findViewById(R.id.status_3);
        statusDes1 = (TextView) findViewById(R.id.status_desc_1);
        statusDes2 = (TextView) findViewById(R.id.status_desc_2);
        statusDes3 = (TextView) findViewById(R.id.status_desc_3);
        lightSwitch = (SwitchCompat) findViewById(R.id.mySwitch);

        String requestAddress = settings.getString(Utils.SHARED_PREFERNECE_IP_ADDRESS, "");
        if (!requestAddress.isEmpty()) {
            requestAddress += Utils.SERVER_REQUEST_APPNAME + Utils.SERVER_REQUEST_SERVER_STATUS;
            recieveStatus(requestAddress);
        }


        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String serverIp = settings.getString(Utils.SHARED_PREFERNECE_IP_ADDRESS, "");
                if (!serverIp.isEmpty()) {
                    String status;
                    if (b) {
                        status = Utils.SERVER_PARAM_VALUE_SWITCH_STATUS_ON;
                    } else {
                        status = Utils.SERVER_PARAM_VALUE_SWITCH_STATUS_OFF;
                    }
                    serverIp += Utils.SERVER_REQUEST_APPNAME + Utils.SERVER_REQUEST_LIGHT_STATUS +
                            Utils.SERVER_REQUEST_PARAMETER_SWITCH_STATUS+status;
                    sendStatus(serverIp);
                } else {
                    Toast.makeText(getBaseContext(), "Server not configured correctly. Please logout and login again",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        //logout action
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(Utils.SHARED_PREFERENCE_LOGGED_IN, false);
                editor.commit();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendStatus(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    private void recieveStatus(final String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    JSONObject resJson = new JSONObject(response);
                                    statusDes1.setText(resJson.getString(Utils.SERVER_STATUS_RESPONSE_STATUS_DESC_1));
                                    status1.setText(resJson.getString(Utils.SERVER_STATUS_RESPONSE_STATUS_1));
                                    statusDes2.setText(resJson.getString(Utils.SERVER_STATUS_RESPONSE_STATUS_DESC_2));
                                    status2.setText(resJson.getString(Utils.SERVER_STATUS_RESPONSE_STATUS_2));
                                    statusDes3.setText(resJson.getString(Utils.SERVER_STATUS_RESPONSE_STATUS_DESC_3));
                                    status3.setText(resJson.getString(Utils.SERVER_STATUS_RESPONSE_STATUS_3));
                                } catch (JSONException e) {
                                    Log.e("getStatus", e.getMessage());
                                }
                                recieveStatus(url);
                            }
                        }, 10000);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

}
