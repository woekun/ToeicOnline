package com.example.woekun.toeiconline.ui.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
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

public class TestActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener {

    private AppController appController;

    private TestPagerAdapter testPagerAdapter;
    private ViewPager mViewPager;
    private MediaPlayer mediaPlayer;
    private SeekBar songProgressBar;
    private LinearLayout audioLayout;
    private ImageButton playButton;
    private TextView songProgressText;

    private onPageChangeListener mOnPageChangeListener = new onPageChangeListener();
    private Handler mHandler = new Handler();

    private ArrayList<ArrayList<Question>> allQuestionForTest;
    private int level;
    private String email;
    private long totalDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appController = AppController.getInstance();

        level = getIntent().getIntExtra(Const.LEVEL, 4);
        setAllQuestionForTest(String.valueOf(level));

        testPagerAdapter = new TestPagerAdapter(getSupportFragmentManager(), allQuestionForTest);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(testPagerAdapter);

        audioLayout = (LinearLayout) findViewById(R.id.seek_bar_view);
        playButton = (ImageButton) findViewById(R.id.play_button);
        songProgressBar = (SeekBar) findViewById(R.id.seek_bar);
        songProgressText = (TextView) findViewById(R.id.time_audio);

        initAudioController(1);
    }

    private void initAudioController(int partId){
        if (checkPart(String.valueOf(partId))) {
            audioLayout.setVisibility(View.VISIBLE);
            mediaPlayer = new MediaPlayer();

            mViewPager.addOnPageChangeListener(mOnPageChangeListener);
            mOnPageChangeListener.onPageSelected(0);
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();
                    else
                        mediaPlayer.pause();
                }
            });
            songProgressBar.setOnSeekBarChangeListener(this);
        } else {
            audioLayout.setVisibility(View.GONE);
        }
    }

    private void setAllQuestionForTest(String level) {
        allQuestionForTest = new ArrayList<>();

        DatabaseHelper databaseHelper = AppController.getInstance().getDatabaseHelper();
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int currentPosition = Utils.progressToTimer(seekBar.getProgress(), (int) totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        totalDuration = mp.getDuration();
    }


    public class onPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position<10) {
                setAudio(allQuestionForTest.get(0).get(position).getAudio());
            }else if(position<40){
                setAudio(allQuestionForTest.get(1).get(position-9).getAudio());
            }else if(position<50){
                setAudio(allQuestionForTest.get(2).get(position-9).getAudio());
            }else if(position<60){
                setAudio(allQuestionForTest.get(2).get(position-9).getAudio());
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
            if(mediaPlayer!=null) {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_test, menu);
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


}
