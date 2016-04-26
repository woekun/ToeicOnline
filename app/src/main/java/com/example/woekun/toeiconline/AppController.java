package com.example.woekun.toeiconline;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.woekun.toeiconline.models.User;
import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;


public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;
    private Picasso picasso;

    private User currentUser = null;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sharedPreferences = getSharedPreferences("Toeic_Online", Activity.MODE_PRIVATE);
        databaseHelper = DatabaseHelper.getInstance(this);
        picasso = Picasso.with(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public Picasso getPicasso() {
        return picasso;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
