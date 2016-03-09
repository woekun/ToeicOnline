package com.example.woekun.toeiconline.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.ui.fragments.TestItem;

import java.util.ArrayList;

public class TestPagerAdapter extends FragmentStatePagerAdapter {

    private static TestPagerAdapter testPagerAdapter;

    private ArrayList<ArrayList<Question>> allQuestionForTest;

    public static TestPagerAdapter getInstance(){
        return testPagerAdapter;
    }

    public TestPagerAdapter(FragmentManager fm, ArrayList<ArrayList<Question>> allQuestionForTest) {
        super(fm);
        testPagerAdapter = this;
        this.allQuestionForTest = allQuestionForTest;
    }

    @Override
    public Fragment getItem(int position) {
        return TestItem.newInstance(position);
    }

    @Override
    public int getCount() {
        return 200;
    }

    public ArrayList<Question> getAllQuestionByPart(int index){
        return allQuestionForTest.get(index);
    }
}
