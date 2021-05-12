package com.truevisionsa.ServerConfig.Model;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ServerConfig.Contract;
import com.truevisionsa.Utils.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerConfigModel implements Contract.ServerConfig.Model {

    private Context context;

    public ServerConfigModel(Context context) {
        this.context = context;
    }

    @Override
    public void checkCon(final onFinishedListener onFinishedListener , Config config) {
        StringRequest strreq = new StringRequest(Request.Method.GET , SingletonRequestQueue.getInstance(context).getUrl() + "Ping?" +
                "con={" +
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {

                onFinishedListener.onFailure(e);
                Log.d("oiuytr", e.toString());
            }

        });

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);


    }
}
