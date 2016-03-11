package com.example.woekun.toeiconline.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.models.Progress;
import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.ui.fragments.QuestionItem;

import java.util.ArrayList;

public class QuestionPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Question> questions;
    private static QuestionPagerAdapter questionPagerAdapter;

    public static QuestionPagerAdapter getInstance(){
        return questionPagerAdapter;
    }

    public QuestionPagerAdapter(FragmentManager fm, ArrayList<Question> questions) {
        super(fm);
        questionPagerAdapter = this;
        this.questions = questions;
    }

    @Override
    public Fragment getItem(int position) {
        return QuestionItem.newInstance(position);
    }


    @Override
    public int getCount() {
        return questions.size();
    }

    public Question getQuestion(int position){
        return questions.get(position);
    }

}
