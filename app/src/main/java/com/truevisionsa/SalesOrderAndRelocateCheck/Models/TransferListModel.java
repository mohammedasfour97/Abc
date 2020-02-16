package com.truevisionsa.SalesOrderAndRelocateCheck.Models;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Order;
import com.truevisionsa.ModelItems.Transfer;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferListModel implements Contract.TransferList.Model {

    private Context context;


    public TransferListModel(Context context) {
        this.context = context;
    }

    @Override
    public void getTransferList(final OnFinishedListener onFinishedListener, String BranchId, String dest_id, Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.getInstance(context).getUrl() +
                "TransferList?BranchId=" + BranchId + "&DestId=" + dest_id + "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}"

                , null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject;
                Transfer transfer;
                List<Transfer> transferList = new ArrayList<>();
                Log.d("zaqwsxcd", String.valueOf(response.length()));
                for (int a = 0 ; a<response.length() ; a++){

                    try {
                        jsonObject = response.getJSONObject(a);

                        transfer = new Transfer();

                        transfer.setDest_branch(jsonObject.getString("DestBranch"));
                        transfer.setTransfer_id(jsonObject.getString("TransferId"));
                        transfer.setItems(String.valueOf(jsonObject.getLong("Items")));
                        transfer.setDest_id(jsonObject.getString("DestId"));

                        transferList.add(transfer);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                onFinishedListener.onFinished(transferList);

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
