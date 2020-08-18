package com.yohannes.app.dev.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbManager = new DbManager(this, null, 1);
        User loggedInUser = dbManager.getLastLoggedinUser();

        settingsTitles = new String[]{"UserName", "Password", "Name", "Father's Name", "Phonenum", "Bio"};
        settingsValue = new String[]{loggedInUser.getUsername(), loggedInUser.getPassword(), loggedInUser.getName(), loggedInUser.getFname(), String.valueOf(loggedInUser.getPhonenum()), loggedInUser.getBio()};

        SettingsListAdapter settingsListAdapter = new SettingsListAdapter(getApplicationContext(), settingsTitles, settingsValue);
        userAvatarimage = findViewById(R.id.settingsAvatarView);
        //ListView settingsListView = (ListView) findViewById(R.id.settings_List);

        TextView userName = findViewById(R.id.settings_username_text);
        TextView password = findViewById(R.id.settings_password_text);
        TextView name = findViewById(R.id.settings_name_text);
        TextView fname = findViewById(R.id.settings_fname_text);
        TextView phonenum = findViewById(R.id.settings_phonenum_text);
        TextView bio = findViewById(R.id.settings_bio_text);


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
