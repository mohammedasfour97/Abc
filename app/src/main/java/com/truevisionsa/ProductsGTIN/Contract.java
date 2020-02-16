package com.truevisionsa.ProductsGTIN;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.ProGTIN;

import org.json.JSONObject;

import java.util.List;

public class Contract {

    public interface ProGTINList {

        interface Model {

        interface OnFinishedListener {

            void onFinished(List<ProGTIN> gtinList);
            void onFinished(JSONObject jsonObject);
            void onFailure(String error);
        }

        void getProGTINList(OnFinishedListener onFinishedListener, String name, Config config);
        void getAddGTIN(OnFinishedListener onFinishedListener , String user_id , String product_id , String scanned_data , Config config);
    }

    interface Presenter{

            void requestProGTINList(String name, Config config);
            void requestAddGTIN(String user_id , String product_id , String scanned_data , Config config);
    }

    interface View{

            void onFinished(String m);
            void onFailure(int error);

            void showProgress();
            void hideProgress();

            void fillRecyclerView(List<ProGTIN> gtinList);

    }

}
}
