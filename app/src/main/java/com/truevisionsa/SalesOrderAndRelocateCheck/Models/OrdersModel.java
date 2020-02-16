package com.truevisionsa.SalesOrderAndRelocateCheck.Models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.truevisionsa.ModelItems.Order;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersModel implements Contract.OrdersList.Model {

    private Context context;

    public OrdersModel(Context context) {
        this.context = context;
    }

    @Override
    public void getOrderList(final OnFinishedListener onFinishedListener, String OrderNo, String BranchId, String UserId, int LoadAll, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.getInstance(context).getUrl() +
                "SalesOrder?OrderNo=" + OrderNo + "&BranchId=" + BranchId + "&UserId=" + UserId + "&LoadAll=" + LoadAll + "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}"

                , null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject;
                Order order;
                List<Order> orderList = new ArrayList<>();
                Log.d("zaqwsxcd", String.valueOf(response.length()));
                for (int a = 0 ; a<response.length() ; a++){

                    try {
                        jsonObject = response.getJSONObject(a);

                        order = new Order();

                        order.setCustomer_id(jsonObject.getString("CustomerId"));
                        order.setCustomer_name(jsonObject.getString("CustomerName"));
                        order.setItems(String.valueOf(jsonObject.getLong("Items")));
                        order.setNote(jsonObject.getString("Notes"));
                        order.setOrder_id(jsonObject.getString("OrderId"));

                        orderList.add(order);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                onFinishedListener.onFinished(orderList);

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

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }
}
