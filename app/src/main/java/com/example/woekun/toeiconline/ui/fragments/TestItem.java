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

public class TestItem extends Fragment {
    private int position;
    private Question question;
    private Integer[] partSize = new Integer[7];

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
        TestPagerAdapter testPagerAdapter = TestPagerAdapter.getInstance();
        for (int i = 0; i < 7; i++) {
            partSize[i] = testPagerAdapter.getQuestionSize(i);
        }

        if (getArguments() != null) {
            position = getArguments().getInt(Const.POSITION);

            if (position < partSize[0]) {
                question = testPagerAdapter.getQuestionByPart(0, position);
            } else if (position < partSize[0] + partSize[1]) {
                question = testPagerAdapter.getQuestionByPart(1, position - partSize[0]);
            } else if (position < partSize[0] + partSize[1] + partSize[2]) {
                question = testPagerAdapter.getQuestionByPart(2, position - partSize[0] - partSize[1]);
            } else if (position < partSize[0] + partSize[1] + partSize[2] + partSize[3]) {
                question = testPagerAdapter.getQuestionByPart(3, position - partSize[0] - partSize[1] - partSize[2]);
            } else if (position < partSize[0] + partSize[1] + partSize[2] + partSize[3] + partSize[4]) {
                question = testPagerAdapter.getQuestionByPart(4, position - partSize[0] - partSize[1] - partSize[2] - partSize[3]);
            } else if (position < partSize[0] + partSize[1] + partSize[2] + partSize[3] + partSize[4] + partSize[5]) {
                question = testPagerAdapter.getQuestionByPart(5, position - partSize[0] - partSize[1] - partSize[2] - partSize[3] - partSize[4]);
            } else if (position < partSize[0] + partSize[1] + partSize[2] + partSize[3] + partSize[4] + partSize[5] + partSize[6]) {
                question = testPagerAdapter.getQuestionByPart(6, position - partSize[0] - partSize[1] - partSize[2] - partSize[3] - partSize[4] - partSize[5]);
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
