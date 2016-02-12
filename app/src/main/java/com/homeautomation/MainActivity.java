package com.homeautomation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


/**
 * Created by hacker on 9/2/16.
 */
public class MainActivity extends Activity {

    private EditText inputIpAddress, inputUsername, inputPassword;
    private TextInputLayout inputLayoutIpAddress, inputLayoutName, inputLayoutPassword;
    private Button btnSignUp;
    private CheckBox loggedIn;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        settings = getSharedPreferences(Utils.SHARED_PREFERENCES, 0);

        inputLayoutIpAddress = (TextInputLayout) findViewById(R.id.input_layout_ipaddress);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputIpAddress = (EditText) findViewById(R.id.input_ipaddress);
        inputUsername = (EditText) findViewById(R.id.input_username);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnSignUp = (Button) findViewById(R.id.btn_submit);
        loggedIn = (CheckBox) findViewById(R.id.loggedIn);

        inputIpAddress.addTextChangedListener(new MyTextWatcher(inputIpAddress));
        inputUsername.addTextChangedListener(new MyTextWatcher(inputUsername));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        inputIpAddress.setText(settings.getString(Utils.SHARED_PREFERNECE_IP_ADDRESS, ""));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        if(!validateIpAddress()) {
            return;
        }

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =inputIpAddress.getText().toString()+Utils.SERVER_REQUEST_APPNAME+
                Utils.SERVER_REQUEST_LOGIN+
                Utils.SERVER_REQUEST_PARAMETER_USERNAME+inputUsername.getText().toString()+
                "&"+Utils.SERVER_REQUEST_PARAMETER_PASSWORD+inputPassword.getText().toString();

        Log.d("url", url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("Success")){
                            SharedPreferences.Editor editor = settings.edit();
                            if(loggedIn.isChecked()) {
                                editor.putBoolean(Utils.SHARED_PREFERENCE_LOGGED_IN, true);
                            } else {
                                editor.putBoolean(Utils.SHARED_PREFERENCE_LOGGED_IN, false);
                            }
                            editor.putString(Utils.SHARED_PREFERNECE_IP_ADDRESS, inputIpAddress.getText().toString());
                            editor.commit();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getBaseContext(),response, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(),"Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private boolean validateIpAddress() {
        boolean flag = false;
        String[] ip = inputIpAddress.getText().toString().split("\\.");
        Log.d("ip validate", ip.toString());
        if(ip.length == 4 && inputIpAddress.getText().toString().contains(":")) {
            flag = true;
        }
        if(!flag) {
            inputLayoutIpAddress.setError("Enter a valid ip address");
            requestFocus(inputIpAddress);
        }
        return flag;
    }

    private boolean validateName() {
        if (inputUsername.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_username));
            requestFocus(inputUsername);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_ipaddress:
                    validateIpAddress();
                    break;
                case R.id.input_username:
                    validateName();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }
}
