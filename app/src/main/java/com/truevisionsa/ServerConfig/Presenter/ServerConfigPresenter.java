package com.truevisionsa.ServerConfig.Presenter;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.R;
import com.truevisionsa.ServerConfig.Contract;
import com.truevisionsa.ServerConfig.Model.ServerConfigModel;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerConfigPresenter implements Contract.ServerConfig.Presenter {

    private Contract.ServerConfig.View view;
    private Contract.ServerConfig.Model model;

    public ServerConfigPresenter(Contract.ServerConfig.View view , Context context) {
        this.view = view;
        this.model = new ServerConfigModel(context);
    }

    @Override
    public void requestCheckCon(Config config) {

        model.checkCon(new Contract.ServerConfig.Model.onFinishedListener() {
            @Override
            public void onFinished(JSONObject result) {

                try {
                    if (!result.getBoolean("Success")) onFailure(result.getString("Message"));
                    else {

                        view.hideProgress();
                        view.onFinished();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object error) {

                view.hideProgress();
                if (error instanceof TimeoutError) view.onFailure(R.string.invalid_api_config);
                else view.onFailure(String.valueOf(error));
            }
        }, config);
    }
}
