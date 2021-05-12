package com.truevisionsa.DTTSTransfer;

import android.content.Context;
import android.text.TextUtils;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.ProGTIN;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.ModelItems.Transfer;
import com.truevisionsa.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DTTSTransferPresenter implements Contract.DTTSTransfer.Presenter {

    private Context context;
    private Contract.DTTSTransfer.Model model;
    private Contract.DTTSTransfer.View view;

    public DTTSTransferPresenter(Context context, Contract.DTTSTransfer.View view) {
        this.context = context;
        this.view = view;
        model = new DTTSTransferModel(context);
    }

    @Override
    public void requestDTTSTransferList(String branchId, String destId, String glnNo, Config config) {
        model.getDTTSTransferList(new Contract.DTTSTransfer.Model.OnFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {

                if (jsonArray.length() == 0) onFailure("");

                else {

                    view.hideProgress();

                    JSONObject jsonObject ;
                    Transfer transfer ;
                    List<Transfer> transferList = new ArrayList<>();

                    for (int a = 0 ; a<jsonArray.length() ; a++) {
                        try {

                            jsonObject = jsonArray.getJSONObject(a);

                            transfer = new Transfer();

                            transfer.setDest_branch(jsonObject.getString("DestBranch"));
                            transfer.setDest_id(jsonObject.getString("DestId"));
                            transfer.setItems(jsonObject.getString("Items"));
                            transfer.setTransfer_id(jsonObject.getString("TransferId"));

                            transferList.add(transfer);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    view.fillRecyclerView(transferList);
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (TextUtils.isEmpty(error)) view.onFailure(R.string.no_items_to_check);
                else view.onFailure(error);

            }
        }, branchId, destId, glnNo, config);
    }

    @Override
    public void requestAddItem(String sourceId, String transferId, String matrixData, String userId, Config config) {

        model.getAddItem(new Contract.DTTSTransfer.Model.OnFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {

                view.hideProgress();

                try {

                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    if (jsonObject.getBoolean("Success")) view.onFinished("add");
                    else onFailure(jsonObject.getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                view.onFailure(error);
            }
        }, sourceId, transferId, matrixData, userId, config);
    }

    @Override
    public void requestAddedProducts(String sourceId, String transferId, Config config) {

        model.getAddedProducts(new Contract.DTTSTransfer.Model.OnFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {

                if (jsonArray.length() == 0) onFailure("");

                else {

                    view.hideProgress();

                    JSONObject jsonObject ;
                    Product product ;
                    List<Product> productList = new ArrayList<>();

                    for (int a = 0 ; a<jsonArray.length() ; a++) {
                        try {

                            jsonObject = jsonArray.getJSONObject(a);

                            product = new Product();

                            product.setProduct_id(jsonObject.getString("ProductId"));
                            product.setId(jsonObject.getString("Id"));
                            product.setProduct_name(jsonObject.getString("Product"));
                            product.setStock_id(String.valueOf(jsonObject.getLong("StockId")));
                            product.setSerial_no(jsonObject.getString("SerialNo"));
                            product.setBatch_no(jsonObject.getString("BatchNo"));

                            productList.add(product);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    view.fillRecyclerView(productList);
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (TextUtils.isEmpty(error)) view.onFailure(R.string.no_items);
                else view.onFailure(error);

            }
        }, sourceId, transferId, config);

    }

    @Override
    public void requestDeleteItem(String id, Config config) {

        model.getDeleteItem(new Contract.DTTSTransfer.Model.OnFinishedListener() {
            @Override
            public void onFinished(JSONArray jsonArray) {

                view.hideProgress();
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    if (jsonObject.getBoolean("Success")) view.onFinished("delete");
                    else onFailure(jsonObject.getString("Message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                view.onFailure(error);
            }
        }, id, config);
    }

}
