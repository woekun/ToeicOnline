package com.example.woekun.toeiconline.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woekun.toeiconline.APIs;
import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.User;
import com.example.woekun.toeiconline.ui.fragments.InformationFragment;
import com.example.woekun.toeiconline.ui.fragments.ListPartFragment;
import com.example.woekun.toeiconline.ui.fragments.RankingFragment;
import com.example.woekun.toeiconline.utils.DialogUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class LobbyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private AppController appController;

    private User currentUser;

    private CircleImageView ava;
    private TextView name;
    private TextView email;
    private DrawerLayout drawer;
    private TextView title;

    private String filePath;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        Intent intent = getIntent();

        appController = AppController.getInstance();
        currentUser = appController.getCurrentUser();
        filePath = currentUser.getAvatar();

        if (intent != null) {
            type = intent.getExtras().getString(Const.TYPE);
            initFragment(type);
        }

        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        ava = (CircleImageView) headerView.findViewById(R.id.imageView);
        ava.setOnClickListener(this);
        filePath = currentUser.getAvatar();
        if (filePath != null && !filePath.equals("")) {
            appController.getPicasso().load(currentUser.getAvatar()).into(ava);
        } else
            appController.getPicasso().load(R.drawable.hughjackman).into(ava);

        name = (TextView) headerView.findViewById(R.id.nav_name);
        name.setText(currentUser.getName());
        name.setOnClickListener(this);

        email = (TextView) headerView.findViewById(R.id.nav_email);
        email.setText(currentUser.getEmail());
        email.setOnClickListener(this);

        title = (TextView) findViewById(R.id.title);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    private void initFragment(String type) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        switch (type) {
            case Const.TRAIN:
                tx.add(R.id.main, ListPartFragment.newInstance());
                break;
            case Const.RANK:
                tx.add(R.id.main, RankingFragment.newInstance());
                break;
            case Const.INFO:
                tx.add(R.id.main, InformationFragment.newInstance());
                break;
        }
        tx.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.nav_training:
                ft.replace(R.id.main, ListPartFragment.newInstance()).commit();
                break;
            case R.id.nav_test:
                DialogUtils.dialogTestConfirm(this, "Are you sure you want to test?", 4, false, false);
                break;
            case R.id.nav_info:
                ft.replace(R.id.main, InformationFragment.newInstance()).commit();
                break;
            case R.id.nav_logout:
                finish();
                Intent intent = new Intent(LobbyActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                appController.getSharedPreferences().edit().clear().apply();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                showFileChooser();
                break;
            case R.id.nav_email:
            case R.id.nav_name:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main, InformationFragment.newInstance()).addToBackStack(null).commit();
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

    private void showFileChooser() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, Const.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();

                final ProgressDialog progressDialog = DialogUtils.dialogUploadImage(getApplicationContext());
                APIs.uploadImage(filePath, currentUser.getEmail(), new APIs.UploadCallback() {
                    @Override
                    public void onSuccess(String message) {
                        progressDialog.dismiss();
                        Toast.makeText(appController, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void getImageLink(String link) {
                        currentUser.setAvatar(link);
                        appController.getPicasso().load(link).into(ava);
                        appController.getDatabaseHelper().updateUser(currentUser);
                    }
                });
            }
        }
    }
}
