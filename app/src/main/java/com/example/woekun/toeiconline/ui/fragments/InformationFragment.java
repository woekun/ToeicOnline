package com.example.woekun.toeiconline.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.woekun.toeiconline.APIs;
import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.User;
import com.example.woekun.toeiconline.ui.activities.LobbyActivity;
import com.example.woekun.toeiconline.ui.activities.LoginActivity;
import com.example.woekun.toeiconline.utils.DialogUtils;


public class InformationFragment extends Fragment implements View.OnClickListener {

    private AppController appController;

    private User currentUser;
    private String filePath;

    private ImageView avatar;
    private TextView name;
    private TextView phone;
    private TextView address;
    private EditText changeName;
    private EditText changePhone;
    private EditText changeAddress;
    private ImageButton editInfo;
    private ImageButton editDone;
    private ProgressDialog progressDialog;

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
        currentUser = appController.getCurrentUser();
        ((LobbyActivity) getActivity()).setTitle("INFORMATION");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        initView(v);
    }

    private void initView(View v) {
        FloatingActionButton btn_logOut = (FloatingActionButton) v.findViewById(R.id.btn_logout);
        btn_logOut.setOnClickListener(this);

        TextView email = (TextView) v.findViewById(R.id.current_email);
        email.setText(currentUser.getEmail());

        name = (TextView) v.findViewById(R.id.current_name);
        if (currentUser.getName() != null)
            name.setText(currentUser.getName());

        phone = (TextView) v.findViewById(R.id.current_phone);
        if (currentUser.getPhone() != null)
            phone.setText(currentUser.getPhone());

        address = (TextView) v.findViewById(R.id.current_address);
        if (currentUser.getAddress() != null)
            address.setText(currentUser.getAddress());

        changeName = (EditText) v.findViewById(R.id.change_current_name);
        changePhone = (EditText) v.findViewById(R.id.change_current_phone);
        changeAddress = (EditText) v.findViewById(R.id.change_current_address);

        avatar = (ImageView) v.findViewById(R.id.current_avatar);
        avatar.setOnClickListener(this);

        filePath = currentUser.getAvatar();
        if (filePath != null && !filePath.equals("")) {
            appController.getPicasso().load(filePath).placeholder(R.mipmap.ic_user_male).into(avatar);
        } else {
            avatar.setImageResource(R.mipmap.ic_user_male);
        }

        editInfo = (ImageButton) v.findViewById(R.id.edit_info);
        editInfo.setOnClickListener(this);
        editDone = (ImageButton) v.findViewById(R.id.edit_done);
        editDone.setOnClickListener(this);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.analyze_container, AnalyzeFragment.newInstance()).commit();
    }

    private void showFileChooser() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, Const.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                appController.getSharedPreferences().edit().clear().apply();
                break;
            case R.id.current_avatar:
                showFileChooser();
                break;
            case R.id.edit_info:
                name.setVisibility(View.GONE);
                changeName.setVisibility(View.VISIBLE);
                phone.setVisibility(View.GONE);
                changePhone.setVisibility(View.VISIBLE);
                address.setVisibility(View.GONE);
                changeAddress.setVisibility(View.VISIBLE);
                editDone.setVisibility(View.VISIBLE);
                editInfo.setVisibility(View.GONE);
                break;
            case R.id.edit_done:
                String addressChange = changeAddress.getText().toString().trim();
                String nameChange = changeName.getText().toString().trim();
                String phoneChange = changePhone.getText().toString().trim();

                if (!addressChange.equals("") || !nameChange.equals("") || !phoneChange.equals("")) {
                    if (!addressChange.equals(""))
                        currentUser.setAddress(addressChange);
                    if (!nameChange.equals(""))
                        currentUser.setName(nameChange);
                    if (!phoneChange.equals(""))
                        currentUser.setPhone(phoneChange);

                    APIs.updateUser(appController, currentUser);
                }

                name.setVisibility(View.VISIBLE);
                name.setText(currentUser.getName());

                phone.setVisibility(View.VISIBLE);
                phone.setText(currentUser.getPhone());

                address.setVisibility(View.VISIBLE);
                address.setText(currentUser.getAddress());

                editInfo.setVisibility(View.VISIBLE);

                editDone.setVisibility(View.GONE);
                changeName.setVisibility(View.GONE);
                changePhone.setVisibility(View.GONE);
                changeAddress.setVisibility(View.GONE);
                break;
            default:
                break;
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
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();

                progressDialog = DialogUtils.dialogUploadImage(getContext());

                APIs.uploadImage(filePath, currentUser.getEmail(), new APIs.UploadCallback() {
                    @Override
                    public void onSuccess(String message) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void getImageLink(String link) {
                        currentUser.setAvatar(link);
                        appController.getPicasso().load(link).into(avatar);
                        appController.getDatabaseHelper().updateUser(currentUser);
                    }
                });
            }
        }
    }
}
