package com.yohannes.app.dev.newsapp;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yohannes.app.dev.newsapp.models.IntroAdapter;

public class IntroActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private int[] layouts = {R.layout.intro_screen4, R.layout.intro_screen1, R.layout.intro_screen2, R.layout.intro_screen3};

    IntroAdapter introAdapter;
    ImageView[] imageViews;

    TextView btn_skip, btn_next;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        btn_skip = (TextView) findViewById(R.id.btn_skip);
        btn_next = (TextView) findViewById(R.id.btn_next);
        linearLayout = (LinearLayout) findViewById(R.id.dotLayout);

        introAdapter = new IntroAdapter(layouts, IntroActivity.this);
        viewPager.setAdapter(introAdapter);

        CreateDots(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                CreateDots(position);
                if (position == layouts.length - 1) {
                    btn_next.setText("Start");
                    btn_skip.setVisibility(View.INVISIBLE);
                } else {
                    btn_next.setText("Next");
                    btn_skip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkipActivity();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int next = viewPager.getCurrentItem() + 1;
                if (next < layouts.length) {
                    viewPager.setCurrentItem(next);
                } else {
                    SkipActivity();
                }

            }
        });
    }


    private void SkipActivity() {
        startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        finish();
    }

    public void CreateDots(int position) {

        if (linearLayout != null) {
            linearLayout.removeAllViews();
        }

        imageViews = new ImageView[layouts.length];

        for (int i = 0; i < layouts.length; i++) {

            imageViews[i] = new ImageView(this);

            if (i == position) {

                imageViews[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
            } else {
                imageViews[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(5, 0, 5, 0);

            linearLayout.addView(imageViews[i], params);

        }
    }
}

