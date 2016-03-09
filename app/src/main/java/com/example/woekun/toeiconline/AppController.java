package com.example.woekun.toeiconline;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;


public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private static AppController mInstance;

    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;
    private Picasso picasso;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sharedPreferences = getSharedPreferences("Toeic_Online", Activity.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);
        picasso = Picasso.with(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    public DatabaseHelper getDatabaseHelper(){
        return databaseHelper;
    }

    public Picasso getPicasso(){
        return picasso;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

//    public ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new LruBitmapCache());
//        }
//        return this.mImageLoader;
//    }

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
