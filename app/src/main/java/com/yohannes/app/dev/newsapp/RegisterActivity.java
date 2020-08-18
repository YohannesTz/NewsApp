package com.yohannes.app.dev.newsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yohannes.app.dev.newsapp.models.User;
import com.yohannes.app.dev.newsapp.util.Util;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    private String username;
    private String password;
    private String name;
    private String fname;
    private String phonenum;
    private String bio;
    private String avatar_link;
    private User _loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText username_field = findViewById(R.id.usernameField);
        final EditText password_field = findViewById(R.id.passworField);
        final EditText name_field = findViewById(R.id.nameField);
        final EditText fname_field = findViewById(R.id.fname);
        final EditText phonenum_field = findViewById(R.id.phoneNumfld);
        final EditText bio_field = findViewById(R.id.bioTextField);
        Button signupButotn = findViewById(R.id.signupButton);


        signupButotn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = username_field.getText().toString();
                password = password_field.getText().toString();
                name = name_field.getText().toString();
                fname = fname_field.getText().toString();

                if (phonenum_field.getText().toString().startsWith("+251") && !phonenum_field.getText().toString().startsWith("09"))
                    phonenum = phonenum_field.getText().toString();
                else
                    phonenum = phonenum = phonenum_field.getText().toString().replace("09", "+251");

                bio = bio_field.getText().toString();
                avatar_link = "https://addisnews.000webhostapp.com/news/images/dude.png"; //just for now

                Log.e("postdata", "Post data ==> " + username + ", " + password + ", " + name + ", " + fname + ", " + phonenum + ", " + bio + ", " + avatar_link);
                new SignupManager().execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public class SignupManager extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;
        private String resultTag = "UnSucessful";
        private HttpURLConnection connection;

        private HttpURLConnection getSignupConneciton(String username, String passsword, String name, String fname, String phonenum, String bio, String avatar_link) {
            try {
                final URL url = new URL(Util.WEBAPIADRESS);
                final HttpURLConnection connection = connectToUrl(url, "POST");
                if (connection != null) {
                    String query = "type=" + URLEncoder.encode("user", Util.ENCODING) + "&username=" + URLEncoder.encode(username, Util.ENCODING) + "&password=" + URLEncoder.encode(passsword, Util.ENCODING) + "&name=" + URLEncoder.encode(name, Util.ENCODING) + "&fname=" + URLEncoder.encode(fname, Util.ENCODING) + "&phonenum=" + URLEncoder.encode(phonenum, Util.ENCODING) + "&bio=" + URLEncoder.encode(bio, Util.ENCODING) + "&avatar_link=" + URLEncoder.encode(avatar_link, Util.ENCODING);
                    Log.e("query", query);
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
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Signing up...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            connection = getSignupConneciton(username, password, name, fname, phonenum, bio, avatar_link);
            if (connection != null) {
                String response = getServerResponse(connection);
                Log.e("newsApp", response);
                if (!Util.isJsonvalid(response)) {
                    Log.e("RegisterActivity", response);
                    resultTag = response.replaceAll("\"", "");
                } else {
                    Log.e("RegisterActivity", "Signup Sucessful!");
                    resultTag = "SignUp Sucessful!";

                }
            } else {
                Log.e("Register", "Conneciton is null Please try again!");
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
            if (connection != null)
                connection.disconnect();
        }


    }
}
