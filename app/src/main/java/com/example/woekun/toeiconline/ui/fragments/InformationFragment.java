package com.example.woekun.toeiconline.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woekun.toeiconline.APIs;
import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.DatabaseHelper;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.User;
import com.example.woekun.toeiconline.ui.activities.LobbyActivity;
import com.example.woekun.toeiconline.ui.activities.LoginActivity;

import java.io.IOException;


public class InformationFragment extends Fragment implements View.OnClickListener {

    private AppController appController;

    private User currentUser;
    private Bitmap bitmap;
    private String mEmail;
    private String filePath;

    private ImageView avatar;
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
        mEmail = appController.getSharedPreferences().getString("email", null);
        if(mEmail!=null) {
            currentUser = appController.getDatabaseHelper().getUser(mEmail);
        }
        ((LobbyActivity)getActivity()).setTitle("INFORMATION");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
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
        avatar = (ImageView)v.findViewById(R.id.current_avatar);
        avatar.setOnClickListener(this);

        filePath = appController.getDatabaseHelper().getAvatar(mEmail);
        if(!filePath.equals("")){
            avatar.setImageBitmap(BitmapFactory.decodeFile(filePath));
        }
    }

    private void showFileChooser() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, Const.PICK_IMAGE_REQUEST);
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
            case R.id.current_avatar:
                showFileChooser();
                break;
            default: break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appController = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if(cursor!=null){
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();

                currentUser.setAvatar(filePath);
                appController.getDatabaseHelper().updateUser(currentUser);
                bitmap = BitmapFactory.decodeFile(filePath);
                avatar.setImageBitmap(bitmap);

                APIs.uploadImage(bitmap, currentUser.getEmail(), currentUser.getEmail() + "_avatar", new APIs.UploadCallback() {
                    @Override
                    public void onRespone(String message) {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
