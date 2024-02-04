package com.truevisionsa.DTTSScan;

import android.content.Context;

import com.truevisionsa.DTTSScan.Contract;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.DTTSList;
import com.truevisionsa.ModelItems.IDName;
import com.truevisionsa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DTTSScanPresenter implements Contract.DTTSScan.Presenter {

    private Contract.DTTSScan.View.MainView mainView;
    private Contract.DTTSScan.View.FragmentView fragmentView;
    private Context context;
    private com.truevisionsa.DTTSScan.Contract.DTTSScan.Model model;

    public DTTSScanPresenter(Contract.DTTSScan.View.MainView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
        this.model = new DTTSScanModel(context);
    }

    public DTTSScanPresenter(Contract.DTTSScan.View.FragmentView fragmentView, Context context) {
        this.fragmentView = fragmentView;
        this.context = context;
        this.model = new DTTSScanModel(context);
    }

    @Override
    public void requestListTypes(Config config) {

        model.getListTypes(new Contract.DTTSScan.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {

                if (jsonArray.length()==0) onFailure("empty_list");

                else {

                    fragmentView.hideProgress();

                    JSONObject jsonObject;

                    IDName idName;

                    List list_types_list = new ArrayList();

                    for (int a=0 ; a<jsonArray.length() ; a++){

                        try {

                            jsonObject = jsonArray.getJSONObject(a);

                            idName = new IDName();
                            idName.setId(jsonObject.getString("Id"));
                            idName.setArbName(jsonObject.getString("NameAr"));
                            idName.setEngName(jsonObject.getString("NameEn"));

                            list_types_list.add(idName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    fragmentView.fillListTypesList(list_types_list);
                }
            }

            @Override
            public void onFinished(JSONObject jsonObject) {
                //////////////////////////
            }

            @Override
            public void onFailure(String error) {

                fragmentView.hideProgress();

                if (error.equals("empty_list")) fragmentView.onFailure(R.string.no_list_types);
                else fragmentView.onFailure(R.string.error_req);
            }
        }, config);
    }

    @Override
    public void requestLists(String BranchId, String UserId, String DeviceId, Config config) {

        model.getLists(new Contract.DTTSScan.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {

                if (jsonArray.length()==0) onFailure("empty_list");

                else {

                    mainView.hideProgress();

                    JSONObject jsonObject;

                    DTTSList dttsList;

                    List<DTTSList> dtts_list_list = new ArrayList();

                    for (int a=0 ; a<jsonArray.length() ; a++){

                        try {

                            jsonObject = jsonArray.getJSONObject(a);

                            dttsList = new DTTSList();
                            dttsList.setId(jsonObject.getString("Id"));
                            dttsList.setRefId(jsonObject.getString("RefId"));
                            dttsList.setTypeName(jsonObject.getString("TypeNameEn"));

                            dtts_list_list.add(dttsList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    mainView.fillListsList(dtts_list_list);
                }
            }

            @Override
            public void onFinished(JSONObject jsonObject) {
                //////////////////////////
            }

            @Override
            public void onFailure(String error) {

                mainView.hideProgress();

                if (error.equals("empty_list")) mainView.onFailure(R.string.no_dtts_lists);
                else mainView.onFailure(R.string.error_req);
            }
        },BranchId,UserId, DeviceId, config);
    }


    @Override
    public void requestAddList(String Branch_id, String DeviceId, String userId, String RefId, String TypeId, Config config) {

        model.getAddList(new Contract.DTTSScan.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {
                //////////////////////////////
            }

            @Override
            public void onFinished(JSONObject jsonObject) {

                try {

                    if (!jsonObject.getBoolean("Success"))
                        onFailure(jsonObject.getString("Message"));

                    else {

                        fragmentView.hideProgress();
                        fragmentView.onListAdded();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                fragmentView.hideProgress();

                if (error.equals("error_req")) fragmentView.onFailure(R.string.error_req);
                else fragmentView.onFailure(error);
            }
        }, Branch_id, DeviceId, userId, RefId, TypeId, config);
    }

    @Override
    public void requestAddedListCount(String ListId, Config config) {

        model.getAddedListCount(new Contract.DTTSScan.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {
                //////////////////////////////
            }

            @Override
            public void onFinished(JSONObject jsonObject) {

                try {

                    if (!jsonObject.getBoolean("Success"))
                        onFailure(jsonObject.getString("Message"));

                    else {

                        mainView.hideProgress();
                        mainView.onListCountGot(jsonObject.getString("Id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                mainView.hideProgress();

                if (error.equals("error_req")) mainView.onFailure(R.string.error_req);
                else mainView.onFailure(error);
            }
        },ListId, config);
    }

    @Override
    public void requestAddItem(String ScanData, String ListId, String userId, Config config) {

        model.getAddItem(new Contract.DTTSScan.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {
                //////////////////////////////
            }

            @Override
            public void onFinished(JSONObject jsonObject) {

                try {

                    if (!jsonObject.getBoolean("Success"))
                        onFailure(jsonObject.getString("Message"));

                    else {

                        mainView.hideProgress();
                        mainView.onItemAdded();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                mainView.hideProgress();

                if (error.equals("error_req")) mainView.onFailure(R.string.error_req);
                else mainView.onFailure(error);
            }
        },ScanData, ListId, userId, config);
    }
}

