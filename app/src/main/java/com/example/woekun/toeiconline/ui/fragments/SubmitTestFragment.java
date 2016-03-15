package com.example.woekun.toeiconline.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.woekun.toeiconline.APIs;
import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.DatabaseHelper;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.User;

public class SubmitTestFragment extends Fragment implements View.OnClickListener {

    private AppController appController;
    private DatabaseHelper databaseHelper;
    private User currentUser;

    private Button submit_button;

    public SubmitTestFragment() {
        appController = AppController.getInstance();
        currentUser = appController.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_submit_test, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submit_button = (Button) view.findViewById(R.id.submit_button);
        submit_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == submit_button) {
            String email = appController.getSharedPreferences().getString(Const.EMAIL, null);
            databaseHelper = appController.getDatabaseHelper();

            long scorePart1 = scoreCalculator(1);
            long scorePart2 = scoreCalculator(2);
            long scorePart3 = scoreCalculator(3);
            long scorePart4 = scoreCalculator(4);
            long scorePart5 = scoreCalculator(5);
            long scorePart6 = scoreCalculator(6);
            long scorePart7 = scoreCalculator(7);

            databaseHelper.addScore(email, scorePart1, scorePart2, scorePart3,
                    scorePart4, scorePart5, scorePart6, scorePart7);

            long totalScore = scorePart1 + scorePart2 + scorePart3 + scorePart4 + scorePart5 + scorePart6 + scorePart7;
            if (totalScore > 0) {
                currentUser.setLevel("2");
            } else if (totalScore > 500) {
                currentUser.setLevel("3");
            } else if (totalScore > 700) {
                currentUser.setLevel("4");
            }

            APIs.updateUser(getContext(), currentUser);
            databaseHelper.updateUser(currentUser);

            databaseHelper.dropTableTestProgress();
        }
    }

    private long scoreCalculator(int part) {
        return databaseHelper.getTrueQuantityByPart(String.valueOf(part));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appController = null;
    }
}
