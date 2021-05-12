package com.truevisionsa.PrintCode;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.Utils.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrintCodeModel implements Contract.PrintCode.Model {

    private Context context;

    public PrintCodeModel(Context context) {
        this.context = context;
    }

    @Override
    public void getProducts(final onFinishedListener onFinishedListener, String BranchId, String CompanyId, String ScanData, String ScanMode, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "PrintBarcode?BranchId=" + BranchId + "&CompanyId=" + CompanyId + "&ScanData=" + ScanData + "&ScanMode=" + ScanMode +
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
    public void getEnterQuantity(final onFinishedListener onFinishedListener, String BranchId, String UserId, String StockId, String Qty, Config config) {

        JsonObjectRequest strreq = new JsonObjectRequest(Request.Method.POST, SingletonRequestQueue.
                getInstance(context).getUrl() +
                "PrintBarcode?BranchId=" + BranchId + "&UserId=" + UserId + "&StockId=" + StockId + "&Qty=" + Qty +
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
    public void getAddedProducts(final onFinishedListener onFinishedListener, String Branch_id, String UserId, String ScanData, Boolean getall, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "PrintBarcode?BranchId=" + Branch_id + "&UserId=" + UserId + "&ScanData=" + ScanData + "&getall=" + getall +
                "&Con={" +
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
    public void getModifyProduct(final onFinishedListener onFinishedListener, String UserId, String Id, String Qty, Config config) {

        JsonObjectRequest strreq = new JsonObjectRequest(Request.Method.PUT, SingletonRequestQueue.
                getInstance(context).getUrl() +
                "PrintBarcode?UserId=" + UserId + "&Id=" + Id + "&Qty=" + Qty  +
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
                params.put("dtput", "oii");
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
