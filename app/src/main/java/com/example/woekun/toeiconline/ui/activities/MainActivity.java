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
import com.example.woekun.toeiconline.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppController appController;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appController = AppController.getInstance();
        sharedPreferences = appController.getSharedPreferences();
        initView();
    }

    private void initView() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        TextView yourRank = (TextView) findViewById(R.id.your_rank);
        yourRank.setOnClickListener(this);

        ImageView general = (ImageView) findViewById(R.id.general);
        general.setOnClickListener(this);

        ImageView to500 = (ImageView) findViewById(R.id.to500);
        to500.setOnClickListener(this);

        ImageView to700 = (ImageView) findViewById(R.id.to700);
        to700.setOnClickListener(this);

        ImageView to900 = (ImageView) findViewById(R.id.to900);
        to900.setOnClickListener(this);

        ImageView test = (ImageView) findViewById(R.id.test);
        test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
        int level = Integer.valueOf(sharedPreferences.getString(Const.LEVEL, "1"));
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
                    Utils.dialogTestConfirm(this, "This level does not fit you!! Do you want to test?", 2);
                }
                break;
            case R.id.to700:
                if (level >= 3) {
                    sharedPreferences.edit().putBoolean(Const.TEST, false).apply();
                    intent.putExtra(Const.TYPE, Const.TRAIN);
                    startActivity(intent);
                } else {
                    Utils.dialogTestConfirm(this, "This level does not fit you!! Do you want to test?", 3);
                }
                break;
            case R.id.to900:
                if (level >= 4) {
                    sharedPreferences.edit().putBoolean(Const.TEST, false).apply();
                    intent.putExtra(Const.TYPE, Const.TRAIN);
                    startActivity(intent);
                } else {
                    Utils.dialogTestConfirm(this, "This level does not fit you!! Do you want to test?", 4);
                }
                break;
            case R.id.general:
                Toast.makeText(MainActivity.this, "update latter", Toast.LENGTH_SHORT).show();
                break;
            case R.id.test:
                Utils.dialogTestConfirm(this, "Are you sure you want to test?", 4);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        appController = null;
    }
}
