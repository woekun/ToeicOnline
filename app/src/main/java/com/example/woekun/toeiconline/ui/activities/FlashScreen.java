package com.example.woekun.toeiconline.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.woekun.toeiconline.APIs;
import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.utils.DialogUtils;

import java.util.ArrayList;

public class FlashScreen extends AppCompatActivity {

    private AppController appController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_screen);

        appController = AppController.getInstance();
        String level = appController.getSharedPreferences().getString(Const.LEVEL, null);
        if (level != null) {
            if (Integer.valueOf(level) == 1) {
                DialogUtils.dialogTestConfirm(this, "First, you need to take a Test", 1);
            } else
                loadData(level);
        }
    }

    private void loadData(String level) {
        APIs.getQuestions(level, new APIs.DataCallBack() {
            @Override
            public void onSuccess(ArrayList<Question> questions) {
                appController.getDatabaseHelper().addQuestion(questions);
                startActivity(new Intent(FlashScreen.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailed(VolleyError error) {
                finish();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        appController = null;
        super.onDestroy();
    }
}
