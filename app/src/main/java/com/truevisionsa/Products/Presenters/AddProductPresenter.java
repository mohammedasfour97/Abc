package com.truevisionsa.Products.Presenters;

import android.content.Context;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.Products.Contract;
import com.truevisionsa.Products.Models.AddProductModel;
import com.truevisionsa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AddProductPresenter implements Contract.AddProducts.Presenter {

    private Contract.AddProducts.View view ;
    private Context context;
    private Contract.AddProducts.Model model ;

    public AddProductPresenter(Contract.AddProducts.View view, Context context) {
        this.view = view;
        this.context = context;
        model = new AddProductModel(context);
    }

    @Override
    public void requestProducts(String branch_id, String company_id, String name, int scan_mode , Config config) {

        model.getProducts(new Contract.AddProducts.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<Product> productList) {

                view.hideProgress(0);

                if (productList.size() == 0) onFailure("no_results");

                view.fillRecyclerView(productList);
            }

            @Override
            public void onFinished(String result) {
                 ////////////////////////////////////////////
            }

            @Override
            public void onFinished(JSONObject result) {
                //////////////////////////////
            }


            @Override
            public void onFailure(String error) {
                view.hideProgress(0);

                if (error.equals("error_req"))
                view.onFailure(R.string.error_req);
                else view.onFailure(R.string.no_results);
            }
        } , branch_id , company_id , name , scan_mode , config);
    }

    @Override
    public void requestAddProduct(String device_id, String ProductId, String StockId, String UnitsInPack, String PacksQty, String UnitsQty,
                                  String StoreId, String BranchId, String userId , Config config) {

        model.addProduct(new Contract.AddProducts.Model.OnFinishedListener() {

            @Override
            public void onFinished(List<Product> productList) {
                ///////////////////////////////////
            }

            @Override
            public void onFinished(String result) {

                view.hideProgress(1);

                if (result.equals("true")) view.onAddFibished();
                else onFailure("err_add_product");
            }

            @Override
            public void onFinished(JSONObject result) {
                //////////////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress(1);

                if (error.equals("error_req"))
                view.onFailure(R.string.error_req);
                else view.onFailure(R.string.error_add_product);
            }
        } , device_id , ProductId , StockId , UnitsInPack , PacksQty , UnitsQty , StoreId , BranchId , userId , config);
    }


    @Override
    public void requestAddOnExistingInv(Config config, String units_in_pack, String pack_qn, String units_qn, String store_id, String user_id,
                                        String inv_id, String old_packs_qn, String old_units_qn) {

        model.addOnExistingInv(new Contract.AddProducts.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<Product> productList) {
                //////////////////////
            }

            @Override
            public void onFinished(String result) {

                view.hideProgress(1);

                    if (result.equals("true")) view.onAddFibished();
                    else onFailure("");
            }

            @Override
            public void onFinished(JSONObject result) {
                ////////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress(1);
                view.onFailure(R.string.error_req);
            }
        } , config , units_in_pack , pack_qn , units_qn , store_id , user_id , inv_id , old_packs_qn , old_units_qn);
    }


    @Override
    public void requestCheckInv(String branch_id , String store_id , String stock_id , Config config) {

        model.checkInv(new Contract.AddProducts.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<Product> productList) {
                ////////////////////
            }

            @Override
            public void onFinished(String result) {
             ////////////////////////////////
            }

            @Override
            public void onFinished(JSONObject result) {

                view.hideProgress(1);

                try {
                    if (result.getBoolean("InventoryExits")){

                        view.showUpdateMessage(result.getString("OldPacksQty") , result.getString("OldUnitsQty") ,
                                result.getString("InventoryId"));
                    }

                    else {

                        view.showAddDialog("yes");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress(1);
                view.onFailure(R.string.error_req);
            }
        } , branch_id , store_id , stock_id , config);
    }


}
