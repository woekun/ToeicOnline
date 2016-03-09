package com.example.woekun.toeiconline.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;

public class SubmitTestFragment extends Fragment implements View.OnClickListener {
    private Button submit_button;
    private AppController appController;

    public SubmitTestFragment() {
        appController = AppController.getInstance();
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
            appController.getDatabaseHelper().addScore(email,scoreCalculator());

        }
    }

    private int scoreCalculator(){
        return 0;
    }
}
