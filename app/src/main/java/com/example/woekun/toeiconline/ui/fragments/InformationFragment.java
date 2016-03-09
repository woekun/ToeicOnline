package com.example.woekun.toeiconline.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.DatabaseHelper;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.User;
import com.example.woekun.toeiconline.ui.activities.LoginActivity;


public class InformationFragment extends Fragment implements View.OnClickListener {

    private AppController appController;

    private User currentUser;
    private TextView name;
    private TextView phone;
    private TextView address;
    private EditText changeName;
    private EditText changePhone;
    private EditText changeAddress;

    public InformationFragment() {
        // Required empty public constructor
    }

    public static InformationFragment newInstance() {
        return new InformationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appController = AppController.getInstance();
        String mEmail = appController.getSharedPreferences().getString("email", null);
        if(mEmail!=null) {
            currentUser = appController.getDatabaseHelper().getUser(mEmail);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_information, container, false);
        Button btn_logOut = (Button)v.findViewById(R.id.btn_logout);
        btn_logOut.setOnClickListener(this);

        TextView email =(TextView) v.findViewById(R.id.current_email);
        email.setText(currentUser.getEmail());

        name = (TextView) v.findViewById(R.id.current_name);
        if(currentUser.getName()!=null)
            name.setText(currentUser.getName());

        phone = (TextView) v.findViewById(R.id.current_phone);
        if(currentUser.getPhone()!=null)
            phone.setText(currentUser.getPhone());

        address = (TextView) v.findViewById(R.id.current_address);
        if(currentUser.getAddress()!=null)
            address.setText(currentUser.getAddress());

        changeName = (EditText) v.findViewById(R.id.change_current_name);
        changePhone = (EditText)v.findViewById(R.id.change_current_phone);
        changeAddress = (EditText) v.findViewById(R.id.change_current_address);

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.current_name:
                name.setVisibility(View.GONE);
                changeName.setVisibility(View.VISIBLE);
                break;
            case R.id.current_phone:
                phone.setVisibility(View.GONE);
                changePhone.setVisibility(View.VISIBLE);
                break;
            case R.id.current_address:
                address.setVisibility(View.GONE);
                changeAddress.setVisibility(View.VISIBLE);
                break;
            default: break;
        }

    }

}
