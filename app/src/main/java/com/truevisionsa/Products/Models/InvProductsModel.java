package com.truevisionsa.Products.Models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.InvProduct;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.Products.Contract;
import com.truevisionsa.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvProductsModel implements Contract.ViewProducts.Model {

    private Context context ;

    public InvProductsModel(Context context) {
        this.context = context;
    }

    @Override
    public void getInvProducts(final OnFinishedListener onFinishedListener, String branch_id, String store_id, String name, boolean load_all,
                               Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "inventory?BranchId=" + branch_id +"&StoreId=" + store_id + "&Name=" + name + "&LoadAll=" + load_all +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject ;
                InvProduct invProduct ;
                List<InvProduct> productList = new ArrayList<>();

                for (int a = 0 ; a<response.length() ; a++) {
                    try {

                        jsonObject = response.getJSONObject(a);

                        invProduct = new InvProduct();

                        invProduct.setExpiry(jsonObject.getString("Expiry"));
                        invProduct.setUnitsInPack(String.valueOf(jsonObject.getInt("UnitsInPack")));
                        invProduct.setInvPacksQty(String.valueOf(jsonObject.getInt("InvPacksQty")));
                        invProduct.setInvUnitsQty(String.valueOf(jsonObject.getInt("InvUnitsQty")));
                        invProduct.setProductHidden(String.valueOf(jsonObject.getBoolean("ProductHidden")));
                        invProduct.setWarePacksQty(String.valueOf(jsonObject.getDouble("WarePacksQty")));
                        invProduct.setWareUnitsQty(String.valueOf(jsonObject.getDouble("WareUnitsQty")));
                        invProduct.setSalePrice(String.valueOf(jsonObject.getDouble("SalePrice")));
                        invProduct.setTotalPrice(String.valueOf(jsonObject.getDouble("TotalPrice")));
                        invProduct.setProductId(String.valueOf(jsonObject.getLong("ProductId")));
                        invProduct.setInventoryId(String.valueOf(jsonObject.getLong("InventoryId")));
                        invProduct.setInvFindId(String.valueOf(jsonObject.getLong("InvFindId")));
                        invProduct.setProductName(jsonObject.getString("ProductName"));
                        invProduct.setUserName(jsonObject.getString("UserName"));
                        invProduct.setStockId(String.valueOf(jsonObject.getLong("StockId")));
                        invProduct.setItem_expired(String.valueOf(jsonObject.getBoolean("ItemExpired")));
                        invProduct.setBatch_no(jsonObject.getString("BatchNo"));

                        productList.add(invProduct);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                onFinishedListener.onFinished(productList);
                Log.d("mnbvc", String.valueOf(productList.size()));

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
    public void editInventory(final OnFinishedListener onFinishedListener, String UnitsInPack, String PacksQty, String UnitsQty,
                              String StoreId, String UserId, String InventoryId, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.PUT , SingletonRequestQueue.getInstance(context).getUrl() +
                "inventory?con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" +
                "&invdataedit={" +
                "\"UnitsInPack\":" + UnitsInPack + "," +
                "\"PacksQty\":" + Integer.parseInt(PacksQty) + "," +
                "\"UnitsQty\":" + UnitsQty + "," +
                "\"StoreId\":" +  StoreId + "," +
                "\"UserId\":" +  UserId + "," +
                "\"InventoryId\":" + InventoryId + "}"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    onFinishedListener.onFinished(String.valueOf(jsonObject.getBoolean("Success")));

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



    @Override
    public void deleteInventory(final OnFinishedListener onFinishedListener, String inventory_id, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.DELETE , SingletonRequestQueue.getInstance(context).getUrl() +
                "inventory?InventoryId=" + inventory_id +
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
                    onFinishedListener.onFinished(String.valueOf(jsonObject.getBoolean("Success")));

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
                params.put("dt","fgh");
                return params;
            }

        };

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }
}

