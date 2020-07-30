package com.yohannes.app.dev.newsapp.models;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yohannes.app.dev.newsapp.R;


/**
 * Created by Yohannes on 27-Mar-20.
 */

public class SettingsListAdapter extends ArrayAdapter {

    private String[] settingsTitle;
    private String[] settingsValue;

    public SettingsListAdapter(Context c, String[] settingsTitle, String[] settingsValue) {
        //super(c, );
        super(c, R.layout.settings_listview_row, R.id.settings_title, settingsTitle);

        this.settingsTitle = settingsTitle;
        this.settingsValue = settingsValue;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.settings_listview_row, null, false);

        TextView settingsTitleText = (TextView) customView.findViewById(R.id.settings_title);
        TextView settingsValueText = (TextView) customView.findViewById(R.id.settings_value);

        settingsTitleText.setText(settingsTitle[position]);
        settingsValueText.setText(settingsValue[position]);

        return customView;
    }
}
