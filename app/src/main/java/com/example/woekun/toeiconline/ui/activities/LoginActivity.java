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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woekun.toeiconline.APIs;
import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.User;
import com.example.woekun.toeiconline.utils.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mEmailSignInButton, btnGG;
    private LoginButton loginFb;
    private Animation animation;

    private CallbackManager callbackManager;
    private AppController appController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appController = AppController.getInstance();

        // Set up the login form.
        initView();
        populateAutoComplete();
    }

    private void initView() {
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        if(mEmailView.getText()!=null || mPasswordView.getText()!=null){
            mEmailView.setText("");
            mPasswordView.setText("");
        }

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    String email = mEmailView.getText().toString();
                    String password = mPasswordView.getText().toString();
                    Login(email, password);
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        loginFb = (LoginButton) findViewById(R.id.btn_facebook);
        loginFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        btnGG = (Button) findViewById(R.id.btn_google);
        animation = AnimationUtils.loadAnimation(this, R.anim.button_translate);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mEmailSignInButton.setAnimation(animation);
        loginFb.startAnimation(animation);
        btnGG.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {
        if (v == mEmailSignInButton) {
            String email = mEmailView.getText().toString();
            String password = mPasswordView.getText().toString();
            Login(email,password);
        }
    }

    private void populateAutoComplete() {
        ArrayList<String> emails = AppController.getInstance().getDatabaseHelper().getAllUsersEmail();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this,
                android.R.layout.simple_dropdown_item_1line, emails);

        mEmailView.setAdapter(adapter);
    }

    public void signUp(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    private void attemptLogin() {

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
            focusView.requestFocus();
        } else {
            Login(email, password);

        }
    }

    private void Login(final String email, final String password) {
        APIs.login(email, password, new APIs.LoginCallBack() {
            @Override
            public void onSuccess(User user) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                if(appController.getDatabaseHelper().getUser(email)==null){
                    appController.getDatabaseHelper().addUser(user);
                }
                appController.getSharedPreferences().edit().putString(Const.EMAIL, email).apply();
                appController.getSharedPreferences().edit().putString(Const.LEVEL, user.getLevel()).apply();
                startActivity(new Intent(LoginActivity.this,FlashScreen.class));
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REG_REQUEST && resultCode == RESULT_OK)
            finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        appController = null;
    }
}

