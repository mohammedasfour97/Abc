package com.truevisionsa.PurchaseCheck;

import android.content.Context;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseCheckPresenter implements Contract.Presenter {

    private Contract.View view;
    private Contract.Model model;
    private Context context;

    public PurchaseCheckPresenter (Contract.View view , Context context){
        this.view = view;
        this.context = context;
        this.model = new PurchaseCheckModel(context);
    }
    @Override
    public void SearchInvoice(String BranchId, String SuppId, String InvoiceNo, Config config) {

        model.getSearchInvoice(new Contract.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONObject jsonObject) {

                try {

                    if (jsonObject.getBoolean("Found")){

                        view.hideProgress();
                        view.getInvoiceId(jsonObject.getString("InvoiceId"));
                    }

                    else onFailure(jsonObject.getString("ErrorMessage"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("err")) view.onFailure(R.string.error_req);
                else view.onFailure(error);
            }
        } , BranchId , SuppId , InvoiceNo , config);
    }

    @Override
    public void AddData(String InvoiceId, String BranchId, String UserId, String ScanData, Config config) {

        model.getAddData(new Contract.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONObject jsonObject) {

                view.hideProgress();

                try {

                    if (jsonObject.getBoolean("Success"))
                    view.onAddSuccessed(String.valueOf(jsonObject.getInt("Id")));
                    else onFailure(jsonObject.getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("err")) view.onFailure(R.string.error_req);
                else view.onFailure(error);
            }
        } , InvoiceId , BranchId , UserId , ScanData , config);
    }
}
