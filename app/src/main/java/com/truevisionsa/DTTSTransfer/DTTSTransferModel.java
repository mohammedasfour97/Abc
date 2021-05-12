package com.truevisionsa.DTTSTransfer;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.ProGTIN;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.ModelItems.Transfer;
import com.truevisionsa.ModelItems.TransferItem;
import com.truevisionsa.Utils.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DTTSTransferModel implements Contract.DTTSTransfer.Model {

    private Context context;

    public DTTSTransferModel(Context context) {
        this.context = context;
    }

    @Override
    public void getDTTSTransferList(final OnFinishedListener onFinishedListener, String branchId, String destId, String glnNo, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "DTTSTransfer?BranchId=" + branchId + "&DestId=" + destId + "&GlnNo=" + glnNo +
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
                onFinishedListener.onFailure(e.getMessage());
            }

        });

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }

    @Override
    public void getAddItem(final OnFinishedListener onFinishedListener, String sourceId, String transferId, String matrixData, String userId, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.POST , SingletonRequestQueue.getInstance(context).getUrl() +
                "DTTSDispatchTransfer?SourceId=" + sourceId + "&TransferId=" + transferId +  "&MatrixData=" + matrixData + "&UserId=" + userId +
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
                    onFinishedListener.onFinished(new JSONArray().put(jsonObject));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                onFinishedListener.onFailure(e.getMessage());
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
    public void getAddedProducts(final OnFinishedListener onFinishedListener, String sourceId, String transferId, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "DTTSDispatchTransfer?SourceId=" + sourceId + "&TransferId=" + transferId+
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
                onFinishedListener.onFailure(e.getMessage());
            }

        });

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }

    @Override
    public void getDeleteItem(final OnFinishedListener onFinishedListener, String id, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.DELETE , SingletonRequestQueue.getInstance(context).getUrl() +
                "DTTSDispatchTransfer?Id=" + id+
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
                    onFinishedListener.onFinished(new JSONArray().put(jsonObject));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                onFinishedListener.onFailure(e.getMessage());
            }

        });

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);
    }
}

