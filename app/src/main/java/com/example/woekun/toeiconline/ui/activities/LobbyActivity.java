package com.example.woekun.toeiconline.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.ui.fragments.AnalyzeFragment;
import com.example.woekun.toeiconline.ui.fragments.InformationFragment;
import com.example.woekun.toeiconline.ui.fragments.ListPartFragment;
import com.example.woekun.toeiconline.ui.fragments.QuestionFragment;
import com.example.woekun.toeiconline.ui.fragments.RankingFragment;

public class LobbyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        initView();
        Intent intent = getIntent();

        if(intent!=null) {
            String s = intent.getExtras().getString(Const.TYPE);
            initFragment(s);
        }
    }

    private void initView(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initFragment(String type) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        switch(type){
            case Const.TRAIN:
                tx.replace(R.id.main, ListPartFragment.newInstance());
                break;
            case Const.RANK:
                tx.replace(R.id.main, RankingFragment.newInstance());
                break;
            case Const.INFO:
                tx.replace(R.id.main, InformationFragment.newInstance());
                break;
        }
        tx.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lobby, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()){
            case R.id.nav_training:
                ft.replace(R.id.main,ListPartFragment.newInstance()).addToBackStack(null).commit();
                break;
            case R.id.nav_test:
                startActivity(new Intent(LobbyActivity.this,TestActivity.class));
                finish();
                break;
            case R.id.nav_info:
                ft.replace(R.id.main,InformationFragment.newInstance()).addToBackStack(null).commit();
                break;
            case R.id.nav_logout:
                finish();
                Intent intent = new Intent(LobbyActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.nav_analyze:
                ft.replace(R.id.main,AnalyzeFragment.newInstance()).addToBackStack(null).commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
