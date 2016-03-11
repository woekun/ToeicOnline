package com.example.woekun.toeiconline.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.util.TypedValue;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.ui.activities.TestActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class Utils {

    public static CountDownTimer countDownTimer(final TextView view) {
         return new CountDownTimer(180000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                view.setText(hms);
            }

            @Override
            public void onFinish() {
                view.setText("00:00:00");
            }
        }.start();
    }

    public static void dialogTestConfirm(final Context context, String message, final int level) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Test confirm");
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(context, TestActivity.class);
                        intent.putExtra(Const.LEVEL, level);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                        AppController.getInstance().getSharedPreferences().edit().putBoolean(Const.TEST, true).apply();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if(level!=4)
                            ((Activity)context).finish();
                        AppController.getInstance().getSharedPreferences().edit().putBoolean(Const.TEST, false).apply();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static int getPixelValue(Context context, int dimenId) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dimenId,
                resources.getDisplayMetrics()
        );
    }

    public static String milliSecondsToTimer(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration;
        totalDuration = (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && email.contains(".");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
    }
}
