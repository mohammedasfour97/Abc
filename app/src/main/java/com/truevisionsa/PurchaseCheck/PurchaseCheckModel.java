package com.truevisionsa.PurchaseCheck;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.PurchaseProduct;
import com.truevisionsa.Utils.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PurchaseCheckModel implements Contract.PurchaseCheck.Model {

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

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);
    }

    @Override
    public void getsearchItem(final onFinishedListener onFinishedListener, String ScanData, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.GET , SingletonRequestQueue.getInstance(context).getUrl() +
                "Purchase?" + "ScanData=" + ScanData +
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

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);
    }

    @Override
    public void getAddData(final onFinishedListener onFinishedListener, String InvoiceId, String BranchId, String UserId, PurchaseProduct purchaseProduct ,
                           String qnt , Config config) {

        StringRequest strreq = new StringRequest(Request.Method.POST , SingletonRequestQueue.getInstance(context).getUrl() +
                "Purchase?" + "con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" +
                "&InsertData={" +
                "\"ProductId\":" + "\"" + purchaseProduct.getProductId() + "\"" +  "," +
                "\"BatchNo\":"  + "\"" + purchaseProduct.getBatchNo() + "\"" + "," +
                "\"Expiry\":" + "\"" + purchaseProduct.getExpiry() + "\"" + "," +
                "\"SerialNo\":" + "\"" + purchaseProduct.getSerialNo() + "\"" + "," +
                "\"Qty\":" + "\"" + qnt + "\""+"," +
                "\"InvoiceId\":" + "\"" + InvoiceId + "\""+"," +
                "\"BranchId\":" + "\"" + BranchId + "\""+"," +
                "\"UserId\":" + "\"" + UserId + "\""+"}"
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

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);
    }
}
