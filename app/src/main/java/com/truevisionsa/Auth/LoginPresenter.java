package com.truevisionsa.Auth;

import android.content.Context;
import android.util.Log;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPresenter implements Contract.Presenter {

    private Contract.View view ;
    private Contract.Model model ;
    private Context context ;

    public LoginPresenter(Contract.View view , Context context) {
        this.view = view;
        this.context = context ;
        model = new LoginModel(context);
    }

    @Override
    public void requestLogin(String user_id, String user_app_verion, String user_device_serial,
                             String user_vpn_con, String user_real_ip, String device_ip, String device_time_zone, String user_connected_to_ip,
                             String user_server_ip, Config config) {

        model.getLogin(new Contract.Model.OnFinishedListener() {
            @Override
            public void onFinished(String result) {
                //////////////////////////

            }

            @Override
            public void onFinished(JSONObject jsonObject) {

                try {
                    if (String.valueOf(jsonObject.getBoolean("Success")).equals("true")){

                        view.hideProgress();
                        view.onLoginFinished(jsonObject.getString("Message"));
                    }

                    else onFailure(jsonObject.getString("Message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("error_req"))
                view.onFailure(R.string.error_req);
                else
                    view.onFailure(error);
            }
        } , user_id , user_app_verion , user_device_serial , user_vpn_con , user_real_ip , device_ip , device_time_zone , user_connected_to_ip ,
                user_server_ip , config);

    }

    @Override
    public void requestCheckUser(String username, String password , String device_id, Config config) {

        model.getCheckUser(new Contract.Model.OnFinishedListener() {
            @Override
            public void onFinished(String result) {

                ///////////////////////////////
            }

            @Override
            public void onFinished(JSONObject jsonObject) {


                try {
                    if (jsonObject.getBoolean("ContainData")){

                        if (jsonObject.getString("UserAccountLock").equals("Y")) onFailure("account_locked");
                        else if (jsonObject.getString("UserPassCanExpired").equals("Y")) onFailure("pass_expired");
                        else view.onCheckFinished(jsonObject.getString("UserId"));
                    }

                    else {

                        onFailure(jsonObject.getString("Message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();
                switch (error){

                    case "Invalid Username or Password" :
                        view.onFailure(R.string.invalied_user_pass);
                        break;

                    case "account_locked" :
                        view.onFailure(R.string.account_locked);
                        break;

                    case "pass_expired" :
                        view.onFailure(R.string.pass_expired);
                        break;

                    case "error_req" :
                        view.onFailure(R.string.error_req);
                        break;

                    case "Device Not Registered" :
                        view.onFailure(R.string.device_not_registered);
                        break;

                    case "Device Is Locked" :
                        view.onFailure(R.string.dev_is_locked);
                        break;

                    case "Device Registeration Period Expired" :
                        view.onFailure(R.string.period_finished);
                        break;
                }
            }
        } , username , password , device_id , config);
    }

    @Override
    public void requestPublicIp() {

        model.getPublicIp(new Contract.Model.OnFinishedListener() {
            @Override
            public void onFinished(String result) {

                view.ongetPublicIpFinished(result);
            }

            @Override
            public void onFinished(JSONObject jsonObject) {
                /////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();
                view.onFailure(R.string.error_req);
            }
        });
    }

    @Override
    public void requestCheckDevice(final String device_id, Config config) {

        model.getCheckDevice(new Contract.Model.OnFinishedListener() {
            @Override
            public void onFinished(String result) {
                ////////////////
            }

            @Override
            public void onFinished(JSONObject jsonObject) {

                view.hideProgress();

                try {
                    if (!jsonObject.getBoolean("DeviceFound")){

                        view.showDialog();
                    }

                    else onFailure("device_already");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("device_already")) view.onFailure(R.string.device_is_already_added);
                else view.onFailure(R.string.error_req);
            }
        } , device_id , config);
    }

    @Override
    public void requestAddDevice(String user_name, String pass, String device_id, String device_name, Config config) {

        model.addDevice(new Contract.Model.OnFinishedListener() {
            @Override
            public void onFinished(String result) {
                ////////////////
            }

            @Override
            public void onFinished(JSONObject jsonObject) {

                try {
                    if (jsonObject.getBoolean("Success")){

                        view.hideProgress();
                        view.onAddFinished();
                    }
                    else onFailure(jsonObject.getString("Message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("error_req")) view.onFailure(R.string.error_req);
                else view.onFailure(error);
            }
        } , user_name , pass , device_id , device_name , config);
    }
}
