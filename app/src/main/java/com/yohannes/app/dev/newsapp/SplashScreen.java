package com.yohannes.app.dev.newsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/*import com.google.android.gms.security.ProviderInstaller;*/
import com.yohannes.app.dev.newsapp.models.IntroAdapter;
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

public class SplashScreen extends AppCompatActivity {

    private DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new CheckDatabase(this).execute();
    }

    public void gotoAcitivity(int activity) {
        if (activity == 1) {
            Intent logInintent = new Intent(this, IntroActivity.class);
            startActivity(logInintent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        } else {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }

    private class CheckDatabase extends AsyncTask<Void, Void, Void> {

        private Context context;

        private CheckDatabase(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dbManager = new DbManager(context, null, 1);
            User user = dbManager.getLastLoggedinUser();

            if (user == null) {
                gotoAcitivity(1);
            } else {
                gotoAcitivity(0);
            }
            return null;
        }
    }

}
