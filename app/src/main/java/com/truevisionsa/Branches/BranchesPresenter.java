package com.truevisionsa.Branches;

import android.content.Context;

import com.truevisionsa.ModelItems.Branch;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Store;
import com.truevisionsa.R;

import java.util.ArrayList;
import java.util.List;

public class BranchesPresenter implements Contract.Presenter {

    private Contract.View view;
    private Context context;
    private Contract.Model model;

    public BranchesPresenter(Contract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new BranchesModel(context);
    }

    @Override
    public void requestBranches(Config config) {

        model.getBraches(new com.truevisionsa.Branches.Contract.Model.OnFinishedListener() {

            @Override
            public void onFinished(List<Branch> branchList) {

                view.fillRecyclerView(branchList);
            }

            @Override
            public void onFinished(String result) {
                //////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.onFailure(R.string.error_req);
            }
        } , config);
    }

    @Override
    public void requestEditDevice(String branch_id, String user_id, String device_id, Config config) {

        model.getEditDevice(new Contract.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<Branch> branchList) {
                ///////////////
            }

            @Override
            public void onFinished(String result) {

                if (!result.equals("true")) onFailure("");
                else view.onFinished();
            }

            @Override
            public void onFailure(String error) {

                view.onFailure(R.string.error_req);
            }
        } , branch_id , user_id , device_id , config);
    }
}
