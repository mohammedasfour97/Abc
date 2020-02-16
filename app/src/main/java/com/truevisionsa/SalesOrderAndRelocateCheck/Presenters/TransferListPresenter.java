package com.truevisionsa.SalesOrderAndRelocateCheck.Presenters;

import android.content.Context;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Transfer;
import com.truevisionsa.R;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.SalesOrderAndRelocateCheck.Models.TransferListModel;

import java.util.List;

public class TransferListPresenter implements Contract.TransferList.Presenter {

    private Contract.TransferList.Model model;
    private Contract.TransferList.View view;
    private Context context;


    public TransferListPresenter(Contract.TransferList.View view, Context context) {
        this.model = new TransferListModel(context);
        this.view = view;
        this.context = context;
    }

    @Override
    public void requestTransferList(String BranchId, String dest_id, Config config) {

        model.getTransferList(new Contract.TransferList.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<Transfer> transferList) {

                if (transferList.size() == 0) onFailure("");
                else {

                    view.hideProgress();
                    view.fillRecyclerView(transferList);
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();
                if (error.isEmpty()) view.onFailure(R.string.no_items_to_check);
                else view.onFailure(R.string.error_req);
            }
        } , BranchId , dest_id , config);
    }
}
