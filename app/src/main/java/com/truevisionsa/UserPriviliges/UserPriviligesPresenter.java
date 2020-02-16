package com.truevisionsa.UserPriviliges;

import android.content.Context;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Store;
import com.truevisionsa.R;

import java.util.List;

public class UserPriviligesPresenter implements Contract.Presenter {

    private Context context;
    private Contract.Model model;
    private Contract.View view;

    public UserPriviligesPresenter(Context context, Contract.View view) {
        this.context = context;
        this.model = new UserPriviligesModel(context);
        this.view = view;
    }

    @Override
    public void requestPrivilige(String user_id, String screen_id, Config config) {

        model.getPrivilige(new Contract.Model.OnFinishedListener() {
            @Override
            public void onFinished(String result) {

                if (result.equals("true")){

                    view.hideProgress();
                    view.onFinished();
                }

                else onFailure("no_enough_priv");
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("error_req")) view.onFailure(R.string.error_req);
                else view.onFailure(R.string.user_privilige_enough);
            }
        } , user_id , screen_id , config);
    }

    @Override
    public void requestlogout(String user_id, String con_id, Config config) {

        model.getlogout(new Contract.Model.OnFinishedListener() {
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

                view.onFailure(R.string.error_req);
            }
        } , user_id , con_id , config);
    }

}
