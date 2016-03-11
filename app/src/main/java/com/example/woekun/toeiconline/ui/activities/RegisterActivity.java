package com.example.woekun.toeiconline.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woekun.toeiconline.APIs;
import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.DatabaseHelper;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.User;
import com.example.woekun.toeiconline.utils.Utils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public final String TAG = RegisterActivity.class.getSimpleName();

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mEmailRegInButton;

    private AppController appController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        appController = AppController.getInstance();

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        mEmailRegInButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailRegInButton.setOnClickListener(this);
    }

    private void attemptSignUp() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !Utils.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Utils.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            Register(email,password);
        }
    }

    private void Register(final String email, String password){
        APIs.register(email, password, new APIs.RegisterCallBack() {
            @Override
            public void onSuccess(User user) {

                startActivityForResult(new Intent(RegisterActivity.this,
                        FlashScreen.class), Const.REG_REQUEST);
                appController.getDatabaseHelper().addUser(user);
                appController.getSharedPreferences().edit().putString(Const.EMAIL, email).apply();
                appController.getSharedPreferences().edit().putString(Const.LEVEL, "1").apply();
                finish();
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mEmailRegInButton)
            attemptSignUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
        appController = null;
    }
}
