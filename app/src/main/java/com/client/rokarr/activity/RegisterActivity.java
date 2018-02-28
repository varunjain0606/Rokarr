package com.client.rokarr.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.client.rokarr.MainActivity;
import com.client.rokarr.R;
import com.client.rokarr.loginandregistration.helper.SQLiteHandler;
import com.client.rokarr.loginandregistration.helper.SessionManager;
import com.client.rokarr.loginandregistration.helper.app.AppConfig;
import com.client.rokarr.loginandregistration.helper.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Varun on 26-11-2015.
 */
public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName, companyName,emailid,contact,tinNo,cstNo,address1,address2,pass;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.clientname);
        companyName = (EditText) findViewById(R.id.company_name);
        pass = (EditText) findViewById(R.id.password1);
        emailid = (EditText) findViewById(R.id.client_email);
        contact = (EditText) findViewById(R.id.contact);
        tinNo = (EditText) findViewById(R.id.tin);
        cstNo = (EditText) findViewById(R.id.cst);
        address1 = (EditText) findViewById(R.id.address1);
        address2 = (EditText) findViewById(R.id.address2);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String company = companyName.getText().toString().trim();
                String mail = emailid.getText().toString().trim();
                String phone = contact.getText().toString().trim();
                String tin = tinNo.getText().toString().trim();
                String cst = cstNo.getText().toString().trim();
                String addres1 = address1.getText().toString().trim();
                String addres2 = address2.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (!name.isEmpty() && !company.isEmpty() && !mail.isEmpty() && !phone.isEmpty() &&!tin.isEmpty()
                        && !cst.isEmpty() && !password.isEmpty()) {
                    registerUser(name, company, mail, phone,password, tin,cst,addres1,addres2);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter mandatory details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String company, final String mail, final String phone,
                              final String password, final String tin, final String cst, final String address1,
                              final String address2) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String contact = user.getString("contact");
                        String companyName = user.getString("company_name");
                        String tin = user.getString("tin");
                        String cst = user.getString("cst");
                        String address1 = user.getString("address1");
                        String address2 = user.getString("address2");
                        String creationDate = user.getString("creation_date");
                        String dt = creationDate;  // Start date
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(sdf.parse(dt));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 365);  // number of days to add
                        dt = sdf.format(c.getTime());  // dt is now the new date

                        // Inserting row in users table
                        db.addUser(name, email, contact, address1, address2, companyName, tin, cst, creationDate, dt);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("companyName", company);
                params.put("password", password);
                params.put("emailID",mail);
                params.put("contact",phone);
                params.put("tin", tin);
                params.put("cst",cst);
                params.put("address1",address1);
                params.put("address2",address2);
                return params;
            }
        };

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

