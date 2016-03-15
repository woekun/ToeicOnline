package com.example.woekun.toeiconline.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.ui.fragments.QuestionItem;

import java.util.ArrayList;

public class QuestionPagerAdapter extends FragmentStatePagerAdapter {

    private static QuestionPagerAdapter questionPagerAdapter;
    private ArrayList<Question> questions;

    public QuestionPagerAdapter(FragmentManager fm, ArrayList<Question> questions) {
        super(fm);
        questionPagerAdapter = this;
        this.questions = questions;
    }

    public static QuestionPagerAdapter getInstance() {
        return questionPagerAdapter;
    }

    @Override
    public Fragment getItem(int position) {
        return QuestionItem.newInstance(position);
    }


    @Override
    public int getCount() {
        return questions.size();
    }

    public Question getQuestion(int position) {
        return questions.get(position);
    }

}
