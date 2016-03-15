package com.example.woekun.toeiconline.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.ui.fragments.SubmitTestFragment;
import com.example.woekun.toeiconline.ui.fragments.TestItem;

import java.util.ArrayList;

public class TestPagerAdapter extends FragmentStatePagerAdapter {

    private static TestPagerAdapter testPagerAdapter;

    private ArrayList<ArrayList<Question>> allQuestionForTest;
    private int numberOfPages;

    public TestPagerAdapter(FragmentManager fm, ArrayList<ArrayList<Question>> allQuestionForTest) {
        super(fm);
        testPagerAdapter = this;
        this.allQuestionForTest = allQuestionForTest;
        numberOfPages = allQuestionForTest.get(0).size()
                + allQuestionForTest.get(1).size()
                + allQuestionForTest.get(2).size()
                + allQuestionForTest.get(3).size()
                + allQuestionForTest.get(4).size()
                + allQuestionForTest.get(5).size()
                + allQuestionForTest.get(6).size()
                + 1;
    }

    public static TestPagerAdapter getInstance() {
        return testPagerAdapter;
    }

    @Override
    public Fragment getItem(int position) {
        if (position != numberOfPages)
            return TestItem.newInstance(position);
        else
            return new SubmitTestFragment();
    }

    @Override
    public int getCount() {
        return numberOfPages;
    }

    public Question getQuestionByPart(int part, int position) {
        return allQuestionForTest.get(part).get(position);
    }

    public int getQuestionSize(int part) {
        return allQuestionForTest.get(part).size();
    }
}
