package com.yohannes.app.dev.newsapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yohannes.app.dev.newsapp.models.SettingsListAdapter;
import com.yohannes.app.dev.newsapp.models.User;
import com.yohannes.app.dev.newsapp.util.CircleAvatar;
import com.yohannes.app.dev.newsapp.util.DbManager;

public class Settings extends AppCompatActivity {

    private String[] settingsTitles;
    private String[] settingsValue;
    private DbManager dbManager;

    ImageView userAvatarimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbManager = new DbManager(this, null, 1);
        User loggedInUser = dbManager.getLastLoggedinUser();

        settingsTitles = new String[]{"UserName", "Password", "Name", "Father's Name", "Phonenum", "Bio"};
        settingsValue = new String[]{loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getName(), loggedInUser.getFname(), String.valueOf(loggedInUser.getPhonenum()), loggedInUser.getBio()};

        SettingsListAdapter settingsListAdapter = new SettingsListAdapter(getApplicationContext(), settingsTitles, settingsValue);
        userAvatarimage = (ImageView) findViewById(R.id.settingsAvatarView);
        //ListView settingsListView = (ListView) findViewById(R.id.settings_List);

        TextView userName = (TextView) findViewById(R.id.settings_username_text);
        TextView password = (TextView) findViewById(R.id.settings_password_text);
        TextView name = (TextView) findViewById(R.id.settings_name_text);
        TextView fname = (TextView) findViewById(R.id.settings_fname_text);
        TextView phonenum = (TextView) findViewById(R.id.settings_phonenum_text);
        TextView bio = (TextView) findViewById(R.id.settings_bio_text);


        Picasso.with(Settings.this).load(loggedInUser.getAvatar_link()).transform(new CircleAvatar()).into(userAvatarimage);

        userName.setText(loggedInUser.getUsername());
        password.setText(loggedInUser.getPassword());
        name.setText(loggedInUser.getName());
        fname.setText(loggedInUser.getFname());
        phonenum.setText(String.valueOf(loggedInUser.getPhonenum()));
        bio.setText(loggedInUser.getBio());

        //settingsListView.setAdapter(settingsListAdapter);

        userAvatarimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changeAvatar = new Intent(getApplicationContext(), ChangeAvatar.class);
                startActivity(changeAvatar);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
