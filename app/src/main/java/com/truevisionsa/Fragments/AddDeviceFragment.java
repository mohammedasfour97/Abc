package com.truevisionsa.Fragments;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.truevisionsa.Auth.LoginActivity;
import com.truevisionsa.R;
import com.truevisionsa.Utils.TinyDB;

public class AddDeviceFragment extends DialogFragment {

    private TextView device_id;
    private Toolbar toolbar;
    private EditText device_name, username;
    private EditText passwordEditText;
    private Button add_device;
    private TinyDB tinyDB;
    private String get_device_id , get_device_name , get_username , get_password;

    public AddDeviceFragment() {
    }

    public static AddDeviceFragment newInstance() {

        AddDeviceFragment addDeviceFragment = new AddDeviceFragment();

        return addDeviceFragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // safety check
            if (getDialog() == null)
                return;

            int dialogWidth = LinearLayout.LayoutParams.MATCH_PARENT;// specify a value here
            int dialogHeight = LinearLayout.LayoutParams.WRAP_CONTENT; // specify a value here

            getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

        }

        // ... other stuff you want to do in your onStart() method
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_device, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);

        setListners();

        tinyDB = new TinyDB(getContext());

        device_id.setText(tinyDB.getString("id"));
    }


    private void initUI(View view) {

        device_id = view.findViewById(R.id.device_id);
        device_name = view.findViewById(R.id.device_name);
        username = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);
        add_device = view.findViewById(R.id.add_device);

    }


    private void setListners() {

        add_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (get_validation()){

                    ((LoginActivity)getActivity()).addDevice(get_username , get_password , get_device_name);
                    dismiss();
                }

                dismiss();
            }
        });
    }

    private boolean get_validation(){

        get_device_id = device_id.getText().toString();
        get_device_name= device_name.getText().toString();
        get_username = username.getText().toString();
        get_password = passwordEditText.getText().toString();

        boolean b = true;

        if (get_device_name.equals("")){

            b = false;
            device_name.setError(getResources().getString(R.string.enter_device_name));
        }

        else if (get_password.equals("")){

            b = false;
            passwordEditText.setError(getResources().getString(R.string.enter_pass));
        }

        else if (get_username.equals("")){

            b = false;
            username.setError(getResources().getString(R.string.enter_username));
        }

        return b;
    }
}