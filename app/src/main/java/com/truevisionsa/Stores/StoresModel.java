package com.truevisionsa.Stores;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Store;
import com.truevisionsa.Utils.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoresModel implements Contract.Model {

    private Context context ;

    public StoresModel(Context context) {
        this.context = context;
    }

    @Override
    public void getStores(final OnFinishedListener onFinishedListener, String branch_id, String user_id,
                          final Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "stores?BranchId=" + branch_id +"&UserId=" + user_id +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject ;
                Store store ;
                List<Store> storeList = new ArrayList<>();

                    for (int a = 0 ; a<response.length() ; a++){

                        try {

                        jsonObject = response.getJSONObject(a);

                        store = new Store(String.valueOf(jsonObject.getLong("StoreId")) , jsonObject.getString("StoreName") ,
                                String.valueOf(jsonObject.getLong("StoreItems")));

                        storeList.add(store);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    onFinishedListener.onFinished(storeList);

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
    public void getlogout(final OnFinishedListener onFinishedListener, String user_id, String con_id, Config config) {

        StringRequest strreq = new StringRequest(Request.Method.PUT , SingletonRequestQueue.getInstance(context).getUrl() +
                "users?UserId=" + user_id + "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}" +
                "&ConId=" + con_id
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    onFinishedListener.onFinished(jsonObject.getString("Success"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {

                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr", e.toString());
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
