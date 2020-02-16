package com.truevisionsa.SalesOrderAndRelocateCheck.Models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.CompareSaleItem;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.SaleItem;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckSalesItemsModel implements Contract.CheckItems.Model {

    private Context context;

    public CheckSalesItemsModel(Context context) {
        this.context = context;
    }

    @Override
    public void getCompareItemList(final OnFinishedListener onFinishedListener, String OrderNo, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "SalesItems?OrderNo=" + OrderNo +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject ;
                CompareSaleItem compareSaleItem;
                List<CompareSaleItem> compareSaleItemList = new ArrayList<>();

                for (int a = 0 ; a<response.length() ; a++) {
                    try {

                        jsonObject = response.getJSONObject(a);
                        Log.d("ytrdfg", jsonObject.toString());

                        compareSaleItem = new CompareSaleItem();

                        compareSaleItem.setPack_qnt(String.valueOf(jsonObject.getLong("PacksQty")));
                        compareSaleItem.setSales_id(jsonObject.getString("SalesId"));
                        compareSaleItem.setStock_id(String.valueOf(jsonObject.getLong("StockId")));

                        compareSaleItemList.add(compareSaleItem);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                onFinishedListener.onFinished(compareSaleItemList);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr_store", e.toString());
            }

        });

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }


    @Override
    public void getSaleItemList(final OnFinishedListener onFinishedListener, String OrderNo, String count_mode, String product, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "SalesItems?OrderNo=" + OrderNo + "&ScanMode=" + count_mode + "&Product=" + product +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject ;
                SaleItem saleItem ;
                List<SaleItem> saleItemList = new ArrayList<>();

                for (int a = 0 ; a<response.length() ; a++) {
                    try {

                        jsonObject = response.getJSONObject(a);
                        Log.d("ytrdfg", jsonObject.toString());

                        saleItem = new SaleItem();

                        saleItem.setBatch(jsonObject.getString("Batch"));
                        saleItem.setExpiry(jsonObject.getString("Expiry"));
                        saleItem.setPacks_qnt(String.valueOf(jsonObject.getLong("PacksQty")));
                        saleItem.setProduct(jsonObject.getString("Product"));
                        saleItem.setProduct_value(String.valueOf(jsonObject.getDouble("PackValue")));
                        saleItem.setSale_id(jsonObject.getString("SalesId"));
                        saleItem.setStock_id(jsonObject.getString("StockId"));

                        saleItemList.add(saleItem);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                onFinishedListener._onFinished(saleItemList);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr_store", e.toString());
            }

        });

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);
    }

    @Override
    public void getSetOrder(final OnFinishedListener onFinishedListener, String order_no, String user_id, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.PUT , SingletonRequestQueue.getInstance(context).getUrl() +
                "SalesOrder" +
                "?con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" +
                "&OrderNo=" + order_no + "&UserId=" + user_id
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

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }
}
