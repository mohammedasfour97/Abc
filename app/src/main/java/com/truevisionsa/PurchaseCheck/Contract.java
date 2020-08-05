package com.truevisionsa.PurchaseCheck;

import com.truevisionsa.ModelItems.Config;

import org.json.JSONObject;

public class Contract {

    public interface Model{

        interface onFinishedListener{

            void onFinished(JSONObject jsonObject);
            void onFailure(String error);
        }
        void getSearchInvoice(onFinishedListener onFinishedListener , String BranchId, String SuppId, String InvoiceNo, Config config);
        void getAddData(onFinishedListener onFinishedListener , String InvoiceId, String BranchId, String UserId, String ScanData , Config config);
    }

    public interface Presenter{

        void SearchInvoice(String BranchId, String SuppId, String InvoiceNo, Config config);
        void AddData(String InvoiceId, String BranchId, String UserId, String ScanData , Config config);
    }

    public interface View{

        void showProgress();
        void hideProgress();

        void onAddSuccessed(String id);
        void getInvoiceId(String id);
        void onFailure(String error);
        void onFailure(int error);
    }
}
