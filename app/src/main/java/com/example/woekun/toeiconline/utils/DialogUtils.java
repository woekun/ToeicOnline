package com.example.woekun.toeiconline.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.ui.activities.MainActivity;
import com.example.woekun.toeiconline.ui.activities.TestActivity;

public class DialogUtils {
    public static void dialogTestConfirm(final Context context, String message, final int level, final boolean callFromMain, final boolean callFromFlash) {
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
                        if (callFromFlash)
                            ((Activity) context).finish();
                        AppController.getInstance().getSharedPreferences().edit().putBoolean(Const.TEST, true).apply();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (level != 4) {
                            if (callFromFlash)
                                (context).startActivity(new Intent(context, MainActivity.class));
                            AppController.getInstance().getSharedPreferences().edit().putBoolean(Const.TEST, false).apply();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void dialogStopTestConfirm(final Context context, final StopTestCallback stopTestCallback) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Stop Test confirm");
        alertDialogBuilder
                .setMessage("Are you sure you want to stop?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stopTestCallback.onAgree();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stopTestCallback.onRefuse();
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static ProgressDialog dialogUploadImage(Context context) {
        return ProgressDialog.show(context, "Uploading...", "Please wait...", false, false);
    }

    public interface StopTestCallback {
        void onAgree();

        void onRefuse();
    }
}
