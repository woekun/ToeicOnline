package com.example.woekun.toeiconline.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.adapters.QuestionPagerAdapter;
import com.example.woekun.toeiconline.customs.QuestionView;
import com.example.woekun.toeiconline.models.Question;

import java.util.ArrayList;


public class QuestionItem extends Fragment {
    private int position;
    private ArrayList<Question> questions;

    public QuestionItem() {
        // Required empty public constructor
    }


    public static QuestionItem newInstance(int position) {
        QuestionItem fragment = new QuestionItem();
        Bundle bundle = new Bundle();
        bundle.putInt(Const.POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questions = QuestionPagerAdapter.getInstance().getQuestions();
            position = getArguments().getInt(Const.POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        QuestionView questionView = (QuestionView) view.findViewById(R.id.test_question_view);
        Question question = questions.get(position);
        questionView.setQuestion(question);
        questionView.setPosition(position);

    }
}
