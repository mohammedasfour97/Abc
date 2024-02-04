package com.truevisionsa.DTTSScan;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.DTTSScan.Contract;
import com.truevisionsa.Utils.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DTTSScanModel implements Contract.DTTSScan.Model {

    private Context context;

    public DTTSScanModel(Context context) {
        this.context = context;
    }

    @Override
    public void getListTypes(onFinishedListener onFinishedListener, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "DTTSTypes?" + "con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                onFinishedListener.onFinished(response);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr_store", e.toString());
            }

        });

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }

    @Override
    public void getLists(onFinishedListener onFinishedListener, String BranchId, String UserId, String DeviceId, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "DTTSList?BranchId="  + BranchId + "&UserId=" + UserId + "&DeviceId=" + DeviceId +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                onFinishedListener.onFinished(response);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr_store", e.toString());
            }

        });

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }

    @Override
    public void getAddList(onFinishedListener onFinishedListener , String Branch_id ,String DeviceId, String userId, String refId, String typeId,
                           Config config) {

        JsonObjectRequest strreq = new JsonObjectRequest(Request.Method.POST, SingletonRequestQueue.
                getInstance(context).getUrl() +
                "DTTSList?BranchId=" + Branch_id + "&DeviceId=" + DeviceId + "&UserId=" + userId + "&RefId=" + refId + "&TypeId=" +
                typeId + "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" + "," +
                "\"ServerPort\":" + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\"" + "}", null, new Response.Listener<JSONObject>() {
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

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dt", "");
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
    public void getAddedListCount(onFinishedListener onFinishedListener, String ListId, Config config) {

        JsonObjectRequest strreq = new JsonObjectRequest(Request.Method.GET, SingletonRequestQueue.
                getInstance(context).getUrl() +
                "DTTSItems?ListId=" + ListId + "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" + "," +
                "\"ServerPort\":" + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\"" + "}", null, new Response.Listener<JSONObject>() {
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

        }) ;

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }

    @Override
    public void getAddItem(onFinishedListener onFinishedListener, String ScanData, String ListId, String userId, Config config) {

        JsonObjectRequest strreq = new JsonObjectRequest(Request.Method.POST, SingletonRequestQueue.
                getInstance(context).getUrl() +
                "DTTSItems?ScanData=" + ScanData + "&ListId=" + ListId + "&UserId=" + userId  +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" + "," +
                "\"ServerPort\":" + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\"" + "}", null, new Response.Listener<JSONObject>() {
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

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dt", "oii");
                return params;
            }

        };

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }
}
