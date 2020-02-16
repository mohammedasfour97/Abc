package com.truevisionsa.SalesOrderAndRelocateCheck.Presenters;

import android.content.Context;

import com.truevisionsa.ModelItems.CompareSaleItem;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.SaleItem;
import com.truevisionsa.R;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.SalesOrderAndRelocateCheck.Models.CheckSalesItemsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CheckSalesItemsPresenter implements Contract.CheckItems.Presenter {

    private Context context;
    private Contract.CheckItems.Model model;
    private Contract.CheckItems.View view;

    public CheckSalesItemsPresenter(Context context, Contract.CheckItems.View view) {
        this.context = context;
        this.view = view;
        this.model = new CheckSalesItemsModel(context);
    }

    @Override
    public void requestCompareItemList(String OrderNo, Config config) {

        model.getCompareItemList(new Contract.CheckItems.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<CompareSaleItem> compareSaleItemList) {

                if (compareSaleItemList.size() == 0) onFailure("no_result");

                else {

                    view.hideProgress();
                    view.fillCompareItems(compareSaleItemList);
                }
            }

            @Override
            public void _onFinished(List<SaleItem> saleItemList) {
                ////////////////////////////
            }

            @Override
            public void onFinish(JSONObject jsonObject) {
                ////////////////////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("no_result")) view.onFailure(R.string.no_items_to_check);
                else view.onFailure(R.string.error_req);
            }
        } , OrderNo , config);
    }

    @Override
    public void requestSaleItemList(String OrderNo, String count_mode, String product, Config config) {

        model.getSaleItemList(new Contract.CheckItems.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<CompareSaleItem> compareSaleItemList) {
                //////////////////////////
            }

            @Override
            public void _onFinished(List<SaleItem> saleItemList) {

                if (saleItemList.size() == 0) onFailure("no_result");

                else {

                    view.hideProgress();
                    view.fillSaleItems(saleItemList);
                }

            }

            @Override
            public void onFinish(JSONObject jsonObject) {
                ///////////////////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("no_result")) view.onFailure(R.string.no_items_to_check);
                else view.onFailure(R.string.error_req);
            }
        } , OrderNo , count_mode , product , config);
    }

    @Override
    public void requestSetOrder(String order_no, String user_id, Config config) {

        model.getSetOrder(new Contract.CheckItems.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<CompareSaleItem> compareSaleItemList) {
                ////////////////////
            }

            @Override
            public void _onFinished(List<SaleItem> saleItemList) {
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
        } , order_no , user_id , config);
    }
}
