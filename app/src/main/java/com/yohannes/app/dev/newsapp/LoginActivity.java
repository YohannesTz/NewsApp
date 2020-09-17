package com.yohannes.app.dev.newsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yohannes.app.dev.newsapp.models.User;
import com.yohannes.app.dev.newsapp.util.DbManager;
import com.yohannes.app.dev.newsapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    //private Button signin_button;
    private TextView signin_button;
    private TextView registerText;

    private String usename;
    private String password;
    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        //signin_button = (Button) findViewById(R.id.email_sign_in_button);
        signin_button = findViewById(R.id.email_sign_in_button);
        registerText = findViewById(R.id.registerTextViw);

        signin_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                usename = mEmailView.getText().toString();
                password = mPasswordView.getText().toString();
                new SignInManager().execute();
            }
        });

        registerText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }
        });
    }

    public void gotoRegister() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void gotoMainAcitivity() {
        Intent registerIntent = new Intent(this, MainActivity.class);
        startActivity(registerIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public class SignInManager extends AsyncTask<Void, Void, Void> {

        private Context context;
        private ProgressDialog progressDialog;
        private String response;
        public boolean isSucessful = false;
        private String resultTag = "UnSucessful";
        private HttpURLConnection connection;

        private HttpURLConnection getLoginConnection(String username, String password) {
            try {
                final URL url = new URL(Util.WEBAPIADRESS);
                final HttpURLConnection connection = connectToUrl(url, "POST");
                if (connection != null) {
                    final String query = String.format("type=%s&username=%s&password=%s", URLEncoder.encode("login", Util.ENCODING), URLEncoder.encode(username, Util.ENCODING), URLEncoder.encode(password, Util.ENCODING));
                    final OutputStream stream = connection.getOutputStream();
                    try {
                        stream.write(query.getBytes(Util.ENCODING));
                    } catch (IOException ex) {
                        Log.e("error writing", ex.toString());
                    }
                }
                return connection;
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.i("eror", ex.toString());
                return null;
            }
        }

        private HttpURLConnection connectToUrl(final URL url, final String method) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(Util.CONNECTION_TIMEOUT);
                connection.setReadTimeout(Util.READ_TIMEOUT);
                if (method.equals("POST")) {
                    connection.setDoOutput(true);
                }
                connection.setRequestMethod(method);

            } catch (IOException e) {
                Log.i("error", "Unable to establish a stable Connection. please try again.");
            }
            return connection;
        }

        private String getServerResponse(final HttpURLConnection connection) {
            try {
                final InputStream stream = connection.getInputStream();
                final BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
                final StringBuilder builder = new StringBuilder();
                final byte[] data = new byte[1024];
                while (bufferedInputStream.read(data) > 0) {
                    builder.append(new String(data));
                    Arrays.fill(data, (byte) 0);
                }
                final String result = builder.toString();
                final StringBuilder fResult = new StringBuilder();
                for (final char chr : result.toCharArray()) {
                    if (chr != '\0') {
                        fResult.append(chr);
                    }
                }
                // return new JSONObject(fResult.toString());
                return fResult.toString();
            } catch (Exception x) {
                Log.i("eror", "Unable to establish a stable Connection. please try again.");
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Signing in...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            connection = getLoginConnection(usename, password);
            if (connection != null) {
                String response = getServerResponse(connection);
                Log.e("newsApp", response);
                if (!Util.isJsonvalid(response)) {
                    resultTag = response.replaceAll("\"", "");
                } else {
                    DbManager dbManager = new DbManager(getApplicationContext(), null, 1);
                    dbManager.addUser(loggedInUser(response));
                    resultTag = "Login Sucessful";
                    gotoMainAcitivity();
                }
            } else {
                Log.e("newsApp", "Conneciton is null Please try again!");
                resultTag = "Connection problem! please try again!";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            showToast(resultTag);
            /*if (connection != null) {
                connection.disconnect();
            }*/
        }

        private User loggedInUser(String serverresult) {
            if (Util.isJsonvalid(serverresult)) {
                try {
                    JSONObject jsonObject = new JSONObject(serverresult);
                    User loggedInUser = new User(jsonObject.getInt("id"), jsonObject.getString("username"), jsonObject.getString("password"), jsonObject.getString("name"), jsonObject.getString("fname"), jsonObject.getInt("phonenum"), jsonObject.getString("bio"), jsonObject.getString("avatar_link"));
                    Log.e("loggedInUser", loggedInUser.toString());
                    return loggedInUser;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}

