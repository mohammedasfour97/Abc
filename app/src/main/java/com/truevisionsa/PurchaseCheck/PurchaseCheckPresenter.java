package com.truevisionsa.PurchaseCheck;

import android.content.Context;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.PurchaseProduct;
import com.truevisionsa.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseCheckPresenter implements Contract.PurchaseCheck.Presenter {

    private Contract.PurchaseCheck.View view;
    private Contract.PurchaseCheck.Model model;
    private Context context;

    public PurchaseCheckPresenter(Contract.PurchaseCheck.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new PurchaseCheckModel(context);
    }

    @Override
    public void SearchInvoice(String BranchId, String SuppId, String InvoiceNo, Config config) {

        model.getSearchInvoice(new Contract.PurchaseCheck.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONObject jsonObject) {

                try {

                    if (jsonObject.getBoolean("Found")) {

                        view.hideProgress();
                        view.getInvoiceId(jsonObject.getString("InvoiceId"));
                    } else onFailure(jsonObject.getString("ErrorMessage"));

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
        }, BranchId, SuppId, InvoiceNo, config);
    }

    @Override
    public void searchItem(String ScanData, Config config) {

        model.getsearchItem(new Contract.PurchaseCheck.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONObject jsonObject) {

                try {

                    if (jsonObject.getBoolean("Found")) {

                        view.hideProgress();

                        PurchaseProduct purchaseProduct = new PurchaseProduct();
                        purchaseProduct.setBatchNo(jsonObject.getString("BatchNo"));
                        purchaseProduct.setExpiry(jsonObject.getString("Expiry"));
                        purchaseProduct.setProductId(jsonObject.getString("ProductId"));
                        purchaseProduct.setProductName(jsonObject.getString("ProductName"));
                        purchaseProduct.setSerialNo(jsonObject.getString("SerialNo"));
                        purchaseProduct.setValidGTIN(jsonObject.getString("ValidGTIN"));

                        view.setProductDetails(purchaseProduct);
                    } else onFailure(jsonObject.getString("Message"));

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
        }, ScanData, config);
    }

    @Override
    public void AddData(String InvoiceId, String BranchId, String UserId, PurchaseProduct purchaseProduct, String qnt ,  Config config) {

        model.getAddData(new Contract.PurchaseCheck.Model.onFinishedListener() {
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
        } , InvoiceId , BranchId , UserId , purchaseProduct , qnt , config);
    }
}
