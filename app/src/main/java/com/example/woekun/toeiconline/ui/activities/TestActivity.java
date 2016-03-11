package com.example.woekun.toeiconline.ui.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.DatabaseHelper;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.adapters.TestPagerAdapter;
import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, OnCompletionListener {

    private AppController appController;

    private TestPagerAdapter testPagerAdapter;
    private ViewPager mViewPager;
    private MediaPlayer mediaPlayer;
    private SeekBar songProgressBar;
    private LinearLayout audioLayout;
    private TextView songProgressText;
    private TextView time_limit;

    private onPageChangeListener mOnPageChangeListener = new onPageChangeListener();
    private Handler mHandler = new Handler();

    private ArrayList<ArrayList<Question>> allQuestionForTest;
    private long totalDuration;
    private int limit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        time_limit = (TextView) findViewById(R.id.time_limit);
        appController = AppController.getInstance();

        int level = getIntent().getIntExtra(Const.LEVEL, 4);
        if (level == 1) {
            setAllQuestionForTest(String.valueOf(4));
        } else {
            setAllQuestionForTest(String.valueOf(level));
        }

        testPagerAdapter = new TestPagerAdapter(getSupportFragmentManager(), allQuestionForTest);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(testPagerAdapter);

        audioLayout = (LinearLayout) findViewById(R.id.seek_bar_view);
        songProgressBar = (SeekBar) findViewById(R.id.seek_bar);
        songProgressText = (TextView) findViewById(R.id.time_audio);

        initAudioController(1);
        Utils.countDownTimer(time_limit);
    }

    private void initAudioController(int partId) {
        if (checkPart(String.valueOf(partId))) {
            audioLayout.setVisibility(View.VISIBLE);
            mediaPlayer = new MediaPlayer();


            mViewPager.addOnPageChangeListener(mOnPageChangeListener);
            if (partId == 1)
                mOnPageChangeListener.onPageSelected(0);

        } else {
            audioLayout.setVisibility(View.GONE);
        }
    }

    private void setAllQuestionForTest(String level) {
        allQuestionForTest = new ArrayList<>();

        DatabaseHelper databaseHelper = appController.getDatabaseHelper();
        for (int i = 1; i < 8; i++) {
            allQuestionForTest.add(databaseHelper.getQuestionsForTest(level, String.valueOf(i)));
        }

    }

    private void setAudio(String audioUri) {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(Const.BASE_AUDIO_URL + audioUri + ".mp3");
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        songProgressBar.setProgress(0);
        songProgressBar.setMax(100);
        updateProgressBar();

    }

    private boolean checkPart(String partId) {
        return Integer.valueOf(partId) <= 4;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        totalDuration = mp.getDuration();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (++limit < 2) {
            songProgressBar.setProgress(0);
            mp.start();
        } else {
            limit = 0;
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
    }

    public class onPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position < 10) {
                setAudio(allQuestionForTest.get(0).get(position).getAudio());
            } else if (position < 40) {
                setAudio(allQuestionForTest.get(1).get(position - 10).getAudio());
            } else if (position < 50) {
                setAudio(allQuestionForTest.get(2).get(position - 10).getAudio());
            } else if (position < 60) {
                setAudio(allQuestionForTest.get(3).get(position - 10).getAudio());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (mediaPlayer != null) {
                long currentDuration = mediaPlayer.getCurrentPosition();

                songProgressText.setText(String.format("%s/%s",
                        Utils.milliSecondsToTimer(currentDuration),
                        Utils.milliSecondsToTimer(totalDuration)));

                // Updating progress bar
                int progress = (Utils.getProgressPercentage(currentDuration, totalDuration));
                songProgressBar.setProgress(progress);

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }
        }
    };


    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        appController = null;
    }

}
