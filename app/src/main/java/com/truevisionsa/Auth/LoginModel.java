package com.truevisionsa.Auth;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.Utils.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginModel implements Contract.Model{

    private Context context ;

    public LoginModel(Context context) {
        this.context = context;
    }

    @Override
    public void getLogin(final OnFinishedListener onFinishedListener, String user_id, String user_app_verion, String user_device_serial,
                         String user_vpn_con, String user_real_ip, String device_ip, String device_time_zone, String user_connected_to_ip,
                         String user_server_ip, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.POST , SingletonRequestQueue.getInstance(context).getUrl() +
                "users?con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" +
                "&value={" +
                "\"UserId\":" + user_id + "," +
                "\"UserAppVersion\":" + "\"" + user_app_verion + "\"" + "," +
                "\"UserDeviceSerial\":" + "\"" + user_device_serial + "\"" + "," +
                "\"UserVPNConnectionUp\":" + user_vpn_con + "," +
                "\"UserRealIp\":" + "\"" + user_real_ip + "\"" + "," +
                "\"UserDeviceIp\":" + "\"" + device_ip + "\"" + "," +
                "\"UserDeviceTimeZone\":" + "\"" + device_time_zone + "\"" + "," +
                "\"UserConnectedToIp\":" + "\"" + user_connected_to_ip + "\"" + "," +
                "\"UserServerIp\":" + "\"" + user_server_ip + "\"}"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    onFinishedListener.onFinished(jsonObject);
                    Log.d("bgyuio", String.valueOf(jsonObject));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {

                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr", e.toString());
            }

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("dt","fgh");
                return params;
            }

        };

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }

    @Override
    public void getCheckDevice(final OnFinishedListener onFinishedListener, String device_id, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.GET , SingletonRequestQueue.getInstance(context).getUrl() +
                "Devices?DeviceId=" + device_id + "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    onFinishedListener.onFinished(jsonObject);
                    Log.d("bgyuio", String.valueOf(jsonObject));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {

                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr", e.toString());
            }

        });

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }

    @Override
    public void addDevice(final OnFinishedListener onFinishedListener, String user_name, String pass, String device_id, String device_name, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.POST , SingletonRequestQueue.getInstance(context).getUrl() +
                "Devices?con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" +
                "&value={" +
                "\"Username\":" + "\"" + user_name + "\"" + "," +
                "\"Password\":" + "\"" + pass + "\"" + "," +
                "\"DeviceName\":" + "\"" + device_name + "\"" + "," +
                "\"DeviceId\":" + "\"" + device_id + "\"}"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    onFinishedListener.onFinished(jsonObject);
                    Log.d("bgyuio", String.valueOf(jsonObject));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {

                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr", e.toString());
            }

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("dt","fgh");
                return params;
            }

        };

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);
    }

    @Override
    public void getCheckUser(final OnFinishedListener onFinishedListener , String username , String password , String device_id, Config config) {

        JsonObjectRequest strreq = new JsonObjectRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "users?Name=" + username +"&Pass=" + password + "&DeviceId=" + device_id +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                onFinishedListener.onFinished(response);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr", e.toString());
            }

        });

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }

    @Override
    public void getPublicIp(final OnFinishedListener onFinishedListener) {

        JsonObjectRequest strreq = new JsonObjectRequest(Request.Method.GET , "https://api6.ipify.org/?format=json",
                null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    onFinishedListener.onFinished(response.getString("ip"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                onFinishedListener.onFinished("unKnown");
                Log.d("oiuytr", e.toString());
            }

        });

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);
    }


}
