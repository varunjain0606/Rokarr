package com.client.rokarr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.client.rokarr.activity.LoginActivity;
import com.client.rokarr.loginandregistration.helper.SQLiteHandler;
import com.client.rokarr.loginandregistration.helper.SessionManager;
import com.client.rokarr.loginandregistration.helper.app.AppConfig;
import com.client.rokarr.loginandregistration.helper.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Varun on 26-11-2015.
 */
public class MainActivity1 extends Activity{
    private TextView txtName,txtDate, txtExpirydate;
    private Button btnLogout, btnmainMenu;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    private String emailid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);


        txtName = (TextView) findViewById(R.id.name);
        txtDate = (TextView)findViewById(R.id.Company_name);
        txtExpirydate = (TextView) findViewById(R.id.valid);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnmainMenu = (Button) findViewById(R.id.mainmenu);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            session.setLogin(false);
            Intent intent = new Intent(MainActivity1.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailid;
                if(!email.isEmpty())
                {
                    checkLogout(email);
                }

            }
        });
        btnmainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity1.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        HashMap<String, String> user = db.getUserDetails();
        if(user != null) {
            String name = user.get("name");
            String date = user.get("creation_date");
            String lastdate = user.get("expiry_date");
            emailid = user.get("email");

            // Displaying the user details on the screen
            if(name != null && date != null && lastdate != null) {
                txtName.setText(name);
                txtDate.setText("Valid from: " + date.trim().substring(0, 10));
                txtExpirydate.setText("Valid upto: " + lastdate);
            }
        }

        Bundle bundle =getIntent().getExtras();
        if (bundle != null)
        {
            boolean flag = bundle.getBoolean("logout");
            if(flag == true)
            {
                btnLogout.performClick();
            }
        }
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */

    private void checkLogout(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_logout";
        pDialog.setMessage("Logging out ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGOUT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(false);

                        // Now store the user in SQLite
                        String status = jObj.getString("status");
                        Toast.makeText(getApplicationContext(),
                                status, Toast.LENGTH_LONG).show();
                        db.deleteUsers();
                        // Launching the login activity
                        Intent intent = new Intent(MainActivity1.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }};

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
