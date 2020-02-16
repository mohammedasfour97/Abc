package com.truevisionsa.ProductsGTIN;

import android.content.Context;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.ProGTIN;
import com.truevisionsa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductsGTINPresenter implements Contract.ProGTINList.Presenter {

    private Context context;
    private Contract.ProGTINList.Model model;
    private Contract.ProGTINList.View view;

    public ProductsGTINPresenter(Context context, Contract.ProGTINList.View view) {
        this.context = context;
        this.view = view;
        model = new ProductsGTINModel(context);
    }

    @Override
    public void requestProGTINList(String name, Config config) {

        model.getProGTINList(new Contract.ProGTINList.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<ProGTIN> gtinList) {

                if (gtinList.size() == 0) onFailure("");

                else {

                    view.hideProgress();
                    view.fillRecyclerView(gtinList);
                }
            }

            @Override
            public void onFinished(JSONObject jsonObject) {
                ///////////////////////////////
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.isEmpty()) view.onFailure(R.string.no_products);
                else view.onFailure(R.string.error_req);
            }
        } , name , config);
    }

    @Override
    public void requestAddGTIN(String user_id, String product_id, String scanned_data, Config config) {

        model.getAddGTIN(new Contract.ProGTINList.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<ProGTIN> gtinList) {
                //////////////////////////
            }

            @Override
            public void onFinished(JSONObject jsonObject) {

                try {
                    if (jsonObject.getBoolean("Success")){

                        view.hideProgress();
                        view.onFinished("");
                    }

                    else onFailure(jsonObject.getString("Message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                switch (error){

                    case "error_req" :
                        view.onFailure(R.string.error_req);
                        break;

                    case "GTIN Update Failed" :
                        view.onFailure(R.string.gtin_update_failed);
                        break;

                    case "SQL Error" :
                        view.onFailure(R.string.sql_error);
                        break;

                    case "Connection Error" :
                        view.onFailure(R.string.connection_error);
                        break;

                    case "Invalid GTIN" :
                        view.onFailure(R.string.invailed_gtin);
                        break;
                }
            }
        } , user_id , product_id , scanned_data , config);
    }
}
