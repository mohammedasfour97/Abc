package com.truevisionsa.Stores;

import android.content.Context;
import android.util.Log;

import com.truevisionsa.ModelItems.Branch;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Store;
import com.truevisionsa.R;

import java.util.ArrayList;
import java.util.List;

public class StoresPresenter implements Contract.Presenter {

    private Contract.View view ;
    private Contract.Model model ;
    private Context context;

    public StoresPresenter(Contract.View view, Context context) {
        this.view = view;
        this.context = context;
        model = new StoresModel(context);
    }

    @Override
    public void requestStores(String branch_id, String user_id, Config config) {

        model.getStores(new Contract.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<Store> storeList) {

                view.hideProgress();

                view.fillRecyclerView(storeList);
            }

            @Override
            public void onFinished(String result) {
                ///////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();
                view.onFailure(error);
            }
        } , branch_id , user_id , config);

    }

    @Override
    public void requestlogout(String user_id, String con_id, Config config) {

        model.getlogout(new Contract.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<Store> storeList) {
                /////////////////////////
            }

            @Override
            public void onFinished(String result) {

                if (result.equals("true")){

                    view.hideProgress();
                    view.onLogoutFinished();
                }
                else onFailure("");
            }

            @Override
            public void onFailure(String error) {

                view.onFailure(error);
            }
        } , user_id , con_id , config);
    }

}
