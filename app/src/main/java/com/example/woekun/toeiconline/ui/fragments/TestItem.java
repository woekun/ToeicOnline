package com.example.woekun.toeiconline.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.adapters.TestPagerAdapter;
import com.example.woekun.toeiconline.customs.QuestionView;
import com.example.woekun.toeiconline.models.Question;

import java.util.ArrayList;

public class TestItem extends Fragment {
    private int position;
    private Question question;

    public TestItem() {

    }

    public static TestItem newInstance(int position) {
        TestItem fragment = new TestItem();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(Const.POSITION);
            TestPagerAdapter testPagerAdapter = TestPagerAdapter.getInstance();
            if (position < 10) {
                question = testPagerAdapter.getQuestionByPart(0, position);
            } else if (position < 40) {
                question = testPagerAdapter.getQuestionByPart(1,position-10);
            } else if (position < 70) {

            } else if (position < 100) {

            } else if (position < 140) {

            } else if (position < 152) {

            } else {

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        QuestionView questionView = (QuestionView) view.findViewById(R.id.test_question_view);
        questionView.setMode(QuestionView.TEST);
        questionView.setQuestion(question);
        questionView.setPosition(position);
    }
}
