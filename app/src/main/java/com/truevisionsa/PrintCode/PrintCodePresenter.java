package com.truevisionsa.PrintCode;

import android.content.Context;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PrintCodePresenter implements Contract.PrintCode.Presenter {

    private Contract.PrintCode.View view;
    private Context context;
    private Contract.PrintCode.Model model;

    public PrintCodePresenter(Contract.PrintCode.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new PrintCodeModel(context);
    }

    @Override
    public void requestProductsBySearchText(String BranchId, String CompanyId, String ScanData, Config config) {

        model.getProducts(new Contract.PrintCode.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {

                if (jsonArray.length()==0) onFailure("empty_list");
                else {

                    view.hideRecyclerProgress();

                    JSONObject jsonObject;
                    Product product;
                    List product_list = new ArrayList();

                    for (int a=0 ; a<jsonArray.length() ; a++){

                        try {
                            jsonObject = jsonArray.getJSONObject(a);

                            product = new Product();
                            product.setStock_id(jsonObject.getString("StockId"));
                            product.setProduct_id(jsonObject.getString("ProductId"));
                            product.setProduct_name(jsonObject.getString("ProductName"));
                            product.setBatch_no(jsonObject.getString("BatchNo"));
                            product.setExpiry(jsonObject.getString("Expiry"));
                            product.setSale_price(jsonObject.getString("SalePrice"));
                            product.setVat(jsonObject.getString("VAT"));

                            product_list.add(product);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    view.fillProducts(product_list);
                }
            }

            @Override
            public void onFinished(JSONObject jsonObject) {
                ///////////////////////////////////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideRecyclerProgress();

                if (error.equals("empty_list")) view.onFailure(R.string.no_products);
                else view.onFailure(R.string.error_req);
            }
        } , BranchId , CompanyId , ScanData , "0" , config);
    }


    @Override
    public void requestProductsByBarCode(String BranchId, String CompanyId, String ScanData, Config config) {

        model.getProducts(new Contract.PrintCode.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {

                if (jsonArray.length()==0) onFailure("empty_list");
                else {

                    view.hideRecyclerProgress();

                    JSONObject jsonObject;
                    Product product;
                    List product_list = new ArrayList();

                    for (int a=0 ; a<jsonArray.length() ; a++){

                        try {
                            jsonObject = jsonArray.getJSONObject(a);

                            product = new Product();
                            product.setStock_id(jsonObject.getString("StockId"));
                            product.setProduct_id(jsonObject.getString("ProductId"));
                            product.setProduct_name(jsonObject.getString("ProductName"));
                            product.setBatch_no(jsonObject.getString("BatchNo"));
                            product.setExpiry(jsonObject.getString("Expiry"));
                            product.setSale_price(jsonObject.getString("SalePrice"));
                            product.setVat(jsonObject.getString("VAT"));

                            product_list.add(product);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    view.fillProducts(product_list);
                }
            }

            @Override
            public void onFinished(JSONObject jsonObject) {
                ///////////////////////////////////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideRecyclerProgress();

                if (error.equals("empty_list")) view.onFailure(R.string.no_products);
                else view.onFailure(R.string.error_req);
            }
        } , BranchId , CompanyId , ScanData , "1" , config);
    }

    @Override
    public void requestEnterQuantity(String BranchId, String UserId, String StockId, String Qty, Config config) {

        model.getEnterQuantity(new Contract.PrintCode.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {
                //////////////////////////////
            }

            @Override
            public void onFinished(JSONObject jsonObject) {

                try {
                    if (!jsonObject.getBoolean("Success")) onFailure(jsonObject.getString("Message"));
                    else {

                        view.hideProgress();
                        view.onFinished("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("error_req")) view.onFailure(R.string.error_req);
                else view.onFailure(error);
            }
        } , BranchId,  UserId,  StockId,  Qty,  config);
    }

    @Override
    public void requestAddedProducts(String Branch_id ,String UserId, String ScanData, Boolean getall, Config config) {

        model.getAddedProducts(new Contract.PrintCode.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {

                if (jsonArray.length()==0) onFailure("empty_list");
                else {

                    view.hideProgress();

                    JSONObject jsonObject;
                    Product product;
                    List product_list = new ArrayList();

                    for (int a=0 ; a<jsonArray.length() ; a++){

                        try {
                            jsonObject = jsonArray.getJSONObject(a);

                            product = new Product();
                            product.setStock_id(jsonObject.getString("StockId"));
                            product.setProduct_id(jsonObject.getString("ProductId"));
                            product.setId(jsonObject.getString("Id"));
                            product.setProduct_name(jsonObject.getString("ProductName"));
                            product.setBatch_no(jsonObject.getString("BatchNo"));
                            product.setExpiry(jsonObject.getString("Expiry"));
                            product.setSale_price(jsonObject.getString("SalePrice"));
                            product.setVat(jsonObject.getString("VAT"));
                            product.setUnits_in_pack(jsonObject.getString("Qty"));

                            product_list.add(product);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    view.fillProducts(product_list);
                }
            }

            @Override
            public void onFinished(JSONObject jsonObject) {
                ///////////////////////////////////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("empty_list")) view.onFailure(R.string.no_products);
                else view.onFailure(R.string.error_req);
            }
        } , Branch_id , UserId , ScanData , getall , config);
    }

    @Override
    public void requestModifyProduct(String UserId, String Id, String Qty, Config config) {

        model.getModifyProduct(new Contract.PrintCode.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {
                //////////////////////////////
            }

            @Override
            public void onFinished(JSONObject jsonObject) {

                try {
                    if (!jsonObject.getBoolean("Success")) onFailure(jsonObject.getString("Message"));
                    else {

                        view.hideProgress();
                        view.onFinished("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("error_req")) view.onFailure(R.string.error_req);
                else view.onFailure(error);
            }
        } , UserId,  Id,  Qty ,  config);
    }

    }

