package com.truevisionsa.Products.Models;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.Products.Contract;
import com.truevisionsa.Utils.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductModel implements Contract.AddProducts.Model {

    private Context context ;

    public AddProductModel(Context context) {
        this.context = context;
    }

    @Override
    public void getProducts(final OnFinishedListener onFinishedListener, String branch_id, String company_id, String name, int scan_mode ,
                            boolean branches_only , Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "products?BranchId=" + branch_id +"&CompanyId=" + company_id + "&Name=" + name + "&ScanMode=" + scan_mode + "&BranchOnly=" + branches_only +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject ;
                Product product ;
                List<Product> productList = new ArrayList<>();

                  for (int a = 0 ; a<response.length() ; a++) {
                      try {

                      jsonObject = response.getJSONObject(a);
                          Log.d("ytrdfg", jsonObject.toString());

                      product = new Product();

                      product.setBatch_no(jsonObject.getString("BatchNo"));
                      product.setExpiry(jsonObject.getString("Expiry"));
                      product.setProduct_hidden(String.valueOf(jsonObject.getBoolean("ProductHidden")));
                      product.setProduct_id(String.valueOf(jsonObject.getLong("ProductId")));
                      product.setProduct_name(jsonObject.getString("ProductName"));
                      product.setSale_price(String.valueOf(jsonObject.getDouble("SalePrice")));
                      product.setStock_id(String.valueOf(jsonObject.getLong("StockId")));
                      product.setUnits_in_pack(String.valueOf(jsonObject.getInt("UnitsInPack")));
                    product.setItem_expired(String.valueOf(jsonObject.getBoolean("ItemExpired")));

                      productList.add(product);

                      } catch (JSONException e) {
                          e.printStackTrace();
                      }

                  }
                    onFinishedListener.onFinished(productList);

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
    public void addProduct(final OnFinishedListener onFinishedListener, String device_id, String ProductId, String StockId, String UnitsInPack,
                           String PacksQty, String UnitsQty, String StoreId, String BranchId, String userId , Config config) {

        StringRequest strreq = new StringRequest(Request.Method.POST , SingletonRequestQueue.getInstance(context).getUrl() +
                "inventory?con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" +
                "&invdata={" +
                "\"ProductId\":" + ProductId + "," +
                "\"StockId\":" + StockId + "," +
                "\"UnitsInPack\":" + UnitsInPack + "," +
                "\"PacksQty\":" + Integer.parseInt(PacksQty) + "," +
                "\"UnitsQty\":" +  UnitsQty + "," +
                "\"StoreId\":" + StoreId + "," +
                "\"BranchId\":" +  BranchId + "," +
                "\"UserId\":" + userId + "," +
                "\"DeviceId\":" + "\"" + device_id + "\"}"
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

        strreq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }


    @Override
    public void addOnExistingInv(final OnFinishedListener onFinishedListener, Config config, String units_in_pack, String pack_qn, String units_qn,
                                 String store_id, String user_id, String inv_id, String old_packs_qn, String old_units_qn) {

        StringRequest strreq = new StringRequest(Request.Method.PUT , SingletonRequestQueue.getInstance(context).getUrl() +
                "invupdate?con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" +
                "&invdataedit={" +
                "\"UnitsInPack\":" + units_in_pack + "," +
                "\"PacksQty\":" + Integer.parseInt(pack_qn) + "," +
                "\"UnitsQty\":" +  units_qn + "," +
                "\"StoreId\":" + store_id + "," +
                "\"UserId\":" + user_id + "," +
                "\"InventoryId\":" + inv_id + "," +
                "\"OldPacksQty\":" + old_packs_qn + "," +
                "\"OldUnitsQty\":" +  old_units_qn + "}"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    onFinishedListener.onFinished(String.valueOf(jsonObject.getBoolean("Success")));
                    Log.d("bvcxcvgo", response.toString());

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



    @Override
    public void checkInv(final OnFinishedListener onFinishedListener, String branch_id, String store_id, String stock_id, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.GET , SingletonRequestQueue.getInstance(context).getUrl() +
                "Invupdate?BranchId=" + branch_id + "&StoreId=" + store_id+ "&StockId=" +stock_id +
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
    }




