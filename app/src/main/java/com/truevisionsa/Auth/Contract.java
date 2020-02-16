package com.truevisionsa.Auth;

import android.content.Context;

import com.truevisionsa.ModelItems.Config;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Contract {

    public interface Model{

        interface OnFinishedListener {

            void onFinished(String result);

            void onFinished (JSONObject jsonObject);

            void onFailure(String error);
        }

        void getCheckUser(OnFinishedListener onFinishedListener , String username , String password , String device_id,  Config config);

        void getPublicIp(OnFinishedListener onFinishedListener );

        void getLogin(OnFinishedListener onFinishedListener , String user_id , String user_app_verion , String user_device_serial ,
                      String user_vpn_con , String user_real_ip , String device_ip , String device_time_zone , String user_connected_to_ip ,
                      String user_server_ip , Config config);

        void getCheckDevice(OnFinishedListener onFinishedListener ,String device_id , Config config);

        void addDevice(OnFinishedListener onFinishedListener , String user_name , String pass , String device_id , String device_name , Config config);


    }

    public interface Presenter{

        void requestLogin(String user_id , String user_app_verion , String user_device_serial , String user_vpn_con , String user_real_ip ,
                          String device_ip , String device_time_zone , String user_connected_to_ip , String user_server_ip , Config config);
        void requestCheckUser(String username , String password , String device_id, Config config);
        void requestPublicIp();
        void requestCheckDevice(String device_id ,Config config);
        void requestAddDevice(String user_name , String pass , String device_id , String device_name , Config config);

    }

    public interface View{

        void onCheckFinished(String title);

        void ongetPublicIpFinished(String title);

        void showDialog();

        void onAddFinished();

        void onLoginFinished(String title);

        void onFailure(int error);

        void onFailure(String error);

        void showProgress();
        void hideProgress();
    }
}
