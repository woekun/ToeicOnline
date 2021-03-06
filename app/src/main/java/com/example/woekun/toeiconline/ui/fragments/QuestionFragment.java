package com.example.woekun.toeiconline.ui.fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.DatabaseHelper;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.adapters.QuestionPagerAdapter;
import com.example.woekun.toeiconline.models.Progress;
import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.models.SubQuestion;
import com.example.woekun.toeiconline.models.User;
import com.example.woekun.toeiconline.ui.activities.LobbyActivity;
import com.example.woekun.toeiconline.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class QuestionFragment extends Fragment implements SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnPreparedListener, View.OnClickListener {

    private AppController appController;
    private DatabaseHelper databaseHelper;
    private QuestionPagerAdapter mSectionsPagerAdapter;
    private Handler mHandler = new Handler();

    private User currentUser;
    private String partId;
    private String level;
    private String email;
    private ArrayList<Question> questions;
    private long totalDuration;

    private MediaPlayer mediaPlayer;
    private SeekBar songProgressBar;
    private LinearLayout audioLayout;
    private ImageButton playButton;
    private TextView songProgressText;
    private onPageChangeListener mOnPageChangeListener = new onPageChangeListener();
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

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static QuestionFragment newInstance(int partId) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("part", partId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appController = AppController.getInstance();
        databaseHelper = appController.getDatabaseHelper();
        currentUser = appController.getCurrentUser();

        if (getArguments() != null) {
            partId = String.valueOf(getArguments().getInt("part"));
            level = currentUser.getLevel();
            email = currentUser.getEmail();

            questions = databaseHelper.getQuestions(level, String.valueOf(partId));
            for (Question question : questions) {
                for (SubQuestion subQuestion : question.getSubQuestionList()) {
                    String subQuestionId = String.valueOf(subQuestion.getSubQuestionID());
                    Progress progress = databaseHelper.getProgress(email, subQuestionId);
                    if (progress != null)
                        subQuestion.setAnswerPicked(progress.getAnswerPicked());
                }
            }
            mSectionsPagerAdapter = new QuestionPagerAdapter(
                    getActivity().getSupportFragmentManager(), questions);

            ((LobbyActivity) getActivity()).setTitle("PART " + partId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        audioLayout = (LinearLayout) view.findViewById(R.id.seek_bar_view);
        playButton = (ImageButton) view.findViewById(R.id.play_button);
        songProgressBar = (SeekBar) view.findViewById(R.id.seek_bar);
        songProgressText = (TextView) view.findViewById(R.id.time_audio);

        if (checkPart(partId)) {
            audioLayout.setVisibility(View.VISIBLE);
            mediaPlayer = new MediaPlayer();

            mViewPager.addOnPageChangeListener(mOnPageChangeListener);
            mOnPageChangeListener.onPageSelected(0);
            playButton.setOnClickListener(this);
            songProgressBar.setOnSeekBarChangeListener(this);
        } else {
            audioLayout.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        if (v == playButton)
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                playButton.setImageResource(R.drawable.ic_pause_circle_fill_24dp);
            } else {
                mediaPlayer.pause();
                playButton.setImageResource(R.drawable.ic_play_circle_fill_24dp);
            }
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onStop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        appController = null;
        super.onDestroy();
    }

    public class onPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setAudio(questions.get(position).getAudio());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
