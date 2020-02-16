package com.truevisionsa.SalesOrderAndRelocateCheck.Presenters;

import android.content.Context;

import com.truevisionsa.ModelItems.CompareTransferItem;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.TransferItem;
import com.truevisionsa.R;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.SalesOrderAndRelocateCheck.Models.CheckTransferItemsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CheckTransferItemsPresenter implements Contract.CheckTransferItems.Presenter {

    private Context context;
    private Contract.CheckTransferItems.Model model;
    private Contract.CheckTransferItems.View view;

    public CheckTransferItemsPresenter(Context context, Contract.CheckTransferItems.View view) {
        this.context = context;
        this.model = new CheckTransferItemsModel(context);
        this.view = view;
    }

    @Override
    public void requestTransferCompareItemList(String Transfer_id, Config config) {

        model.getTransferCompareItemList(new Contract.CheckTransferItems.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<CompareTransferItem> checkTransferItemsList) {

                if (checkTransferItemsList.size() == 0) onFailure("");
                else {

                    view.hideProgress();
                    view.fillTransferCompareItems(checkTransferItemsList);
                }
            }

            @Override
            public void _onFinished(List<TransferItem> transferItemList) {
                //////////////////////
            }

            @Override
            public void onFinish(JSONObject jsonObject) {
                //////////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.isEmpty()) view.onFailure(R.string.no_items_to_check);
                else view.onFailure(R.string.error_req);
            }
        } , Transfer_id , config);
    }

    @Override
    public void requestTransferItemList(String transfer_id, String count_mode, String product, Config config) {

        model.getTransferItemList(new Contract.CheckTransferItems.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<CompareTransferItem> checkTransferItemsList) {
                //////////////////////
            }

            @Override
            public void _onFinished(List<TransferItem> transferItemList) {

                if (transferItemList.size() == 0) onFailure("");
                else {

                    view.hideProgress();
                    view.fillTransferItems(transferItemList);
                }
            }

            @Override
            public void onFinish(JSONObject jsonObject) {
                ////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();
                if (error.isEmpty()) view.onFailure(R.string.no_items_to_check);
                else view.onFailure(R.string.error_req);
            }
        } , transfer_id , count_mode , product , config);
    }

    @Override
    public void requestSetTransfer(String transfer_id, String user_id, Config config) {

        model.getSetTransfer(new Contract.CheckTransferItems.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<CompareTransferItem> checkTransferItemsList) {
                ////////////////////////
            }

            @Override
            public void _onFinished(List<TransferItem> transferItemList) {
                ///////////////////////////////////////
            }

            @Override
            public void onFinish(JSONObject jsonObject) {
                try {

                    if (jsonObject.getBoolean("Success")){

                        view.hideProgress();
                        view.onFinished();
                    }

                    else {

                        onFailure("");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();
                view.onFailure(R.string.error_req);
            }
        } , transfer_id , user_id , config);
    }
}
