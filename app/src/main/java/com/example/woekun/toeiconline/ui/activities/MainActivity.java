package com.example.woekun.toeiconline.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.User;
import com.example.woekun.toeiconline.utils.DialogUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppController appController;
    private SharedPreferences sharedPreferences;
    private User currentUser;

    private ImageView practice;
    private ImageView test;
    private ImageView to500;
    private ImageView to700;
    private ImageView to900;
    private ImageView logo;

    private boolean isCollapse;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appController = AppController.getInstance();
        currentUser = appController.getCurrentUser();
        sharedPreferences = appController.getSharedPreferences();
        initView();
    }

    private void initView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        TextView yourRank = (TextView) findViewById(R.id.your_rank);
        yourRank.setOnClickListener(this);

        logo = (ImageView) findViewById(R.id.logo);

        practice = (ImageView) findViewById(R.id.practice);
        practice.setOnClickListener(this);

        to500 = (ImageView) findViewById(R.id.to500);
        to500.setOnClickListener(this);

        to700 = (ImageView) findViewById(R.id.to700);
        to700.setOnClickListener(this);

        to900 = (ImageView) findViewById(R.id.to900);
        to900.setOnClickListener(this);

        test = (ImageView) findViewById(R.id.test);
        test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
        int level = Integer.valueOf(currentUser.getLevel());
        switch (v.getId()) {
            case R.id.fab:
                intent.putExtra(Const.TYPE, Const.INFO);
                startActivity(intent);
                break;
            case R.id.your_rank:
                intent.putExtra(Const.TYPE, Const.RANK);
                startActivity(intent);
                break;
            case R.id.to500:
                if (level >= 2) {
                    sharedPreferences.edit().putBoolean(Const.TEST, false).apply();
                    intent.putExtra(Const.TYPE, Const.TRAIN);
                    startActivity(intent);
                } else {
                    DialogUtils.dialogTestConfirm(this, "This level does not fit you!! Do you want to test?", 2, true, false);
                }
                break;
            case R.id.to700:
                if (level >= 3) {
                    sharedPreferences.edit().putBoolean(Const.TEST, false).apply();
                    intent.putExtra(Const.TYPE, Const.TRAIN);
                    startActivity(intent);
                } else {
                    DialogUtils.dialogTestConfirm(this, "This level does not fit you!! Do you want to test?", 3, true, false);
                }
                break;
            case R.id.to900:
                if (level >= 4) {
                    sharedPreferences.edit().putBoolean(Const.TEST, false).apply();
                    intent.putExtra(Const.TYPE, Const.TRAIN);
                    startActivity(intent);
                } else {
                    DialogUtils.dialogTestConfirm(this, "This level does not fit you!! Do you want to test?", 4, true, false);
                }
                break;
            case R.id.test:
                DialogUtils.dialogTestConfirm(this, "Are you sure you want to test?", 4, true, false);
                break;
            case R.id.practice:
                if (!isCollapse) {
                    isCollapse = true;
                    to500.setVisibility(View.VISIBLE);
                    to700.setVisibility(View.VISIBLE);
                    to900.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.GONE);
                } else {
                    isCollapse = false;
                    to500.setVisibility(View.GONE);
                    to700.setVisibility(View.GONE);
                    to900.setVisibility(View.GONE);
                    logo.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        i = 0;
    }

    @Override
    public void onBackPressed() {
        i++;
        if (i == 1) {
            Toast.makeText(MainActivity.this, "Press back once more to exit.", Toast.LENGTH_SHORT).show();
        } else if (i > 1) {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appController = null;
    }
}
