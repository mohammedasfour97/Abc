package com.truevisionsa.PurchaseCheck;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PurchaseCheckModel implements Contract.Model {

    private Context context;

    public PurchaseCheckModel(Context context) {
        this.context = context;
    }

    @Override
    public void getSearchInvoice(final onFinishedListener onFinishedListener, String BranchId, String SuppId, String InvoiceNo, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.GET , SingletonRequestQueue.getInstance(context).getUrl() +
                "Purchase?" + "BranchId=" + BranchId + "&SuppId=" + SuppId + "&InvoiceNo=" + InvoiceNo +
                "&con={" +
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

                onFinishedListener.onFailure("err");
            }

        });

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);
    }

    @Override
    public void getAddData(final onFinishedListener onFinishedListener, String InvoiceId, String BranchId, String UserId, String ScanData, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.POST , SingletonRequestQueue.getInstance(context).getUrl() +
                "Purchase?" + "con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" + "&InvoiceId=" + InvoiceId + "&BranchId=" + BranchId +
                "&UserId=" + UserId + "&ScanData=" + ScanData
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

                onFinishedListener.onFailure("err");
            }

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("dt","fgh");
                return params;
            }

        };

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);
    }
}
