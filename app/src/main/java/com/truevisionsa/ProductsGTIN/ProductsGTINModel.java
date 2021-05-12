package com.truevisionsa.ProductsGTIN;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.ProGTIN;
import com.truevisionsa.Utils.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsGTINModel implements Contract.ProGTINList.Model {

    private Context context;

    public ProductsGTINModel(Context context) {
        this.context = context;
    }

    @Override
    public void getProGTINList(final OnFinishedListener onFinishedListener, String name, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "ProductsGTIN?Name=" + name +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject ;
                ProGTIN proGTIN ;
                List<ProGTIN> proGTINArrayList = new ArrayList<>();

                for (int a = 0 ; a<response.length() ; a++) {
                    try {

                        jsonObject = response.getJSONObject(a);

                        proGTIN = new ProGTIN();

                        proGTIN.setGtin(jsonObject.getString("GTIN"));
                        proGTIN.setProduct(jsonObject.getString("Product"));
                        proGTIN.setProduct_id(String.valueOf(jsonObject.getLong("ProductId")));
                        proGTIN.setValid_gtin(String.valueOf(jsonObject.getBoolean("ValidGTIN")));


                        proGTINArrayList.add(proGTIN);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                onFinishedListener.onFinished(proGTINArrayList);
                Log.d("mnbvc", String.valueOf(proGTINArrayList.size()));

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
    public void getAddGTIN(final OnFinishedListener onFinishedListener, String user_id, String product_id, String scanned_data, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.PUT , SingletonRequestQueue.getInstance(context).getUrl() +
                "ProductsGTIN?con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" +
                "&UserId=" + user_id + "&ProductId=" + product_id + "&ScannedData=" + scanned_data
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    onFinishedListener.onFinished(jsonObject);
                    Log.d("plmjuyhgv", jsonObject.toString());

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

