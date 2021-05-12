package com.truevisionsa.DTTSTransfer;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.ProGTIN;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Contract {

    public interface DTTSTransfer {

        interface Model {

        interface OnFinishedListener {

            void onFinished(JSONArray jsonArray);
            void onFailure(String error);
        }

        void getDTTSTransferList(OnFinishedListener onFinishedListener, String branchId, String destId, String glnNo, Config config);
        void getAddItem(OnFinishedListener onFinishedListener, String sourceId, String transferId, String matrixData, String userId, Config config);
        void getAddedProducts(OnFinishedListener onFinishedListener , String sourceId, String transferId, Config config);
        void getDeleteItem(OnFinishedListener onFinishedListener , String id , Config config);
    }

    interface Presenter{

        void requestDTTSTransferList(String branchId, String destId, String glnNo, Config config);
        void requestAddItem(String sourceId, String transferId, String matrixData, String userId, Config config);
        void requestAddedProducts(String sourceId, String transferId, Config config);
        void requestDeleteItem(String id , Config config);
    }

    interface View{

            void onFinished(String m);
            void onFailure(int error);
            void onFailure(String error);

            void showProgress();
            void hideProgress();

            void fillRecyclerView(List list);

    }

}
}
