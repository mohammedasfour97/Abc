package com.truevisionsa.SalesOrderAndRelocateCheck.Models;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.CompareTransferItem;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.TransferItem;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.Utils.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckTransferItemsModel implements Contract.CheckTransferItems.Model {

    private Context context;

    public CheckTransferItemsModel(Context context) {
        this.context = context;
    }

    @Override
    public void getTransferCompareItemList(final OnFinishedListener onFinishedListener, String Transfer_id, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "TransferItems?TransferId=" + Transfer_id +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject ;
                CompareTransferItem compareTransferItem;
                List<CompareTransferItem> compareTransferItemList = new ArrayList<>();

                for (int a = 0 ; a<response.length() ; a++) {
                    try {

                        jsonObject = response.getJSONObject(a);
                        Log.d("ytrdfg", jsonObject.toString());

                        compareTransferItem = new CompareTransferItem();

                        compareTransferItem.setPack_qnt(String.valueOf(jsonObject.getInt("PacksQty")));
                        compareTransferItem.setTrans_id(jsonObject.getString("TransId"));
                        compareTransferItem.setStock_id(String.valueOf(jsonObject.getLong("StockId")));
                        compareTransferItem.setUnits_in_pack(String.valueOf(jsonObject.getInt("UnitsQty")));

                        compareTransferItemList.add(compareTransferItem);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                onFinishedListener.onFinished(compareTransferItemList);

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
    public void getTransferItemList(final OnFinishedListener onFinishedListener, String transfer_id, String count_mode, String product, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "TransferItems?TransferId=" + transfer_id + "&ScanMode=" + count_mode + "&Product=" + product +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject ;
                TransferItem transferItem ;
                List<TransferItem> transferItemList = new ArrayList<>();

                for (int a = 0 ; a<response.length() ; a++) {
                    try {

                        jsonObject = response.getJSONObject(a);
                        Log.d("ytrdfg", jsonObject.toString());

                        transferItem = new TransferItem();

                        transferItem.setBatch(jsonObject.getString("Batch"));
                        transferItem.setExpiry(jsonObject.getString("Expiry"));
                        transferItem.setPack_qnt(String.valueOf(jsonObject.getInt("PacksQty")));
                        transferItem.setProduct(jsonObject.getString("Product"));
                        transferItem.setPack_value(String.valueOf(jsonObject.getDouble("PackValue")));
                        transferItem.setTrans_id(jsonObject.getString("TransId"));
                        transferItem.setStock_id(jsonObject.getString("StockId"));
                        transferItem.setUnits_in_pack(String.valueOf(jsonObject.getInt("UnitsInPack")));
                        transferItem.setUnits_qnt(String.valueOf(jsonObject.getInt("UnitsQty")));

                        transferItemList.add(transferItem);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                onFinishedListener._onFinished(transferItemList);

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
    public void getSetTransfer(final OnFinishedListener onFinishedListener, String transfer_id, String user_id, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.PUT , SingletonRequestQueue.getInstance(context).getUrl() +
                "TransferList" +
                "?con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" +
                "&TransferId=" + transfer_id + "&UserId=" + user_id
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    onFinishedListener.onFinish(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr_store", e.toString());
            }

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("dtput","fgh");
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

