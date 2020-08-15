package com.yohannes.app.dev.newsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/*import com.google.android.gms.security.ProviderInstaller;*/
import com.yohannes.app.dev.newsapp.models.News;
import com.yohannes.app.dev.newsapp.models.User;
import com.yohannes.app.dev.newsapp.util.DbManager;
import com.yohannes.app.dev.newsapp.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class SplashScreen extends AppCompatActivity {

    private DbManager dbManager;
    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new CheckDatabase(this).execute();
    }

    public void gotoAcitivity(int activity, News initdata) {
        if (activity == 1) {
            Intent logInintent = new Intent(this, IntroActivity.class);
            logInintent.putExtra("InitialData", news);
            startActivity(logInintent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        } else {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            mainActivityIntent.putExtra("InitialData", news);
            startActivity(mainActivityIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }

    private class CheckDatabase extends AsyncTask<String, String, String> {

        private Context context;
        String result;

        private CheckDatabase(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(Util.WEBAPIADRESS);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String temp;

                    while ((temp = reader.readLine()) != null) {
                        stringBuilder.append(temp);
                    }
                    result = stringBuilder.toString();
                    Log.i("result", result);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (result == null) {
                    Toast.makeText(this.context, "Connection Problem Tryagain!", Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finishAffinity();
                } else {
                    Log.e("resultreal", result);
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    news = new News(jsonObject.getInt("id"), jsonObject.getString("uploader"), jsonObject.getString("newsHeader"), jsonObject.getString("newsContent"), jsonObject.getString("detailImage"), jsonObject.getString("date"), jsonObject.getInt("view"));

                    dbManager = new DbManager(context, null, 1);
                    User user = dbManager.getLastLoggedinUser();

                    if (user == null) {
                        gotoAcitivity(1, news);
                    } else {
                        gotoAcitivity(0, news);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
