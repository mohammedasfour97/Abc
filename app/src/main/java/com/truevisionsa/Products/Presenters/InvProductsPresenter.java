package com.truevisionsa.Products.Presenters;

import android.content.Context;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.InvProduct;
import com.truevisionsa.Products.Contract;
import com.truevisionsa.Products.Models.InvProductsModel;
import com.truevisionsa.R;

import java.util.List;

public class InvProductsPresenter implements Contract.ViewProducts.Presenter {

    private Contract.ViewProducts.View view ;
    private Context context ;
    private Contract.ViewProducts.Model model ;

    public InvProductsPresenter(Contract.ViewProducts.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new InvProductsModel(context);
    }

    @Override
    public void requestInvProducts(String branch_id, String store_id, String name, Config config) {

        boolean load_all = false;
        if (name.isEmpty()) load_all = true;

        model.getInvProducts(new Contract.ViewProducts.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<InvProduct> productList) {

                view.hideProgress();
                view.fillRecyclerView(productList);
            }

            @Override
            public void onFinished(String result) {
                //////////////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();
                view.onFailure(R.string.error_req);
            }
        } , branch_id , store_id , name , load_all , config);
    }


    @Override
    public void requestEditInventory(String UnitsInPack, String PacksQty, String UnitsQty, String StoreId, String UserId, String InventoryId,
                                     Config config) {

        model.editInventory(new Contract.ViewProducts.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<InvProduct> productList) {
                ////////////////////////////
            }

            @Override
            public void onFinished(String result) {

                view.hideProgress();

                if (result.equals("true")) view.onEditFinished();
                else onFailure("error_edit");
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();
                view.onFailure(R.string.error_edit_inv);
            }
        } , UnitsInPack , PacksQty , UnitsQty , StoreId , UserId , InventoryId , config);
    }


    @Override
    public void requestDeleteInventory(String inventory_id, Config config) {

        model.deleteInventory(new Contract.ViewProducts.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<InvProduct> productList) {
                ////////////////////////////
            }

            @Override
            public void onFinished(String result) {

                view.hideProgress();

                if (result.equals("true")) view.onDeleteFinished();
                else onFailure("error_delete");
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();
                view.onFailure(R.string.error_delete_inv);
            }
        } , inventory_id , config);
    }
}
