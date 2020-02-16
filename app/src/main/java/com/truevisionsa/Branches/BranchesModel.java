package com.truevisionsa.Branches;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.truevisionsa.ModelItems.Branch;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BranchesModel implements Contract.Model {

    private Context context;

    public BranchesModel(Context context) {
        this.context = context;
    }

    @Override
    public void getEditDevice(final OnFinishedListener onFinishedListener, String branch_id, String user_id, String device_id, Config config) {


        Log.d("zaqwesxde", SingletonRequestQueue.getInstance(context).getUrl() +
                "devices?BranchId=" + branch_id +  "&UserId=" + user_id + "&DeviceId=" + device_id + "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + "," +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}");

        StringRequest strreq = new StringRequest(Request.Method.PUT , SingletonRequestQueue.getInstance(context).getUrl() +
                "devices?BranchId=" + branch_id +  "&UserId=" + user_id + "&DeviceId=" + device_id + "&con={" +
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
                    onFinishedListener.onFinished(jsonObject.getString("Success"));
                    Log.d("oiuytr", jsonObject.toString());

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

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);
    }



    @Override
    public void getBraches(final OnFinishedListener onFinishedListener , Config config) {

        JsonArrayRequest strreq = new JsonArrayRequest(Request.Method.GET , SingletonRequestQueue.
                getInstance(context).getUrl() +
                "branches?IP=" + config.getServerIp() +
                "&con={" +
                "\"ServerIP\":" + "\"" + config.getServerIp() + "\"" +  "," +
                "\"ServerPort\":"  + config.getServerPort() + ", " +
                "\"ServerUserName\":" + "\"" + config.getServerUserName() + "\"" + "," +
                "\"ServerPassword\":" + "\"" + config.getServerPassword() + "\"" + "," +
                "\"DefaultSchema\":" + "\"" + config.getDefaultSchema() + "\""+ "}", null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject ;
                Branch branch ;
                ArrayList<Branch> branchList = new ArrayList<>();

                try {

                    for (int a = 0; a<response.length(); a++){

                        jsonObject = response.getJSONObject(a);

                        branch = new Branch();
                        branch.setBranch_id(String.valueOf(jsonObject.getLong("BranchId")));
                        branch.setBranch_ip4(jsonObject.getString("BranchIP4"));
                        branch.setBranch_name(jsonObject.getString("BranchName"));
                        branch.setBranch_uuid(jsonObject.getString("BranchUUID"));
                        branch.setCompany_id(String.valueOf(jsonObject.getLong("CompanyId")));

                        branchList.add(branch);
                    }


                    onFinishedListener.onFinished(branchList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                onFinishedListener.onFailure("error_req");
                Log.d("oiuytr_branch", e.toString());
            }

        });

        SingletonRequestQueue.getInstance(context).getRequestQueue().add(strreq);

    }
}
