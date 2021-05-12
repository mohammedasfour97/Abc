package com.truevisionsa.PurchaseCheck;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.PurchaseProduct;

import org.json.JSONObject;

public class Contract {

    public static class PurchaseCheck {

        public interface Model {

            interface onFinishedListener {

                void onFinished(JSONObject jsonObject);

                void onFailure(String error);
            }

            void getSearchInvoice(onFinishedListener onFinishedListener, String BranchId, String SuppId, String InvoiceNo, Config config);

            void getsearchItem(onFinishedListener onFinishedListener, String ScanData, Config config);

            void getAddData(onFinishedListener onFinishedListener, String InvoiceId, String BranchId, String UserId, PurchaseProduct purchaseProduct,
                            String qnt , Config config);
        }

        public interface Presenter {

            void SearchInvoice(String BranchId, String SuppId, String InvoiceNo, Config config);

            void searchItem(String ScanData, Config config);

            void AddData(String InvoiceId, String BranchId, String UserId, PurchaseProduct purchaseProduct, String qnt, Config config);
        }

        public interface View {

            void showProgress();

            void hideProgress();

            void onAddSuccessed(String id);

            void getInvoiceId(String id);

            void setProductDetails(PurchaseProduct product);

            void onFailure(String error);

            void onFailure(int error);
        }

    }

    public static class PurchaseOrderCheck {

        public interface Model {

            interface onFinishedListener {

                void onFinished(JSONObject jsonObject);

                void onFailure(String error);
            }

            void getSearchInvoice(onFinishedListener onFinishedListener, String BranchId, String SuppId, String InvoiceNo, String POId , Config config);

            void getsearchItem(onFinishedListener onFinishedListener , String POId, String ScanData, Config config);

            void getAddData(onFinishedListener onFinishedListener, String InvoiceId, String BranchId, String UserId, PurchaseProduct purchaseProduct,
                            String qnt, Config config);
        }

        public interface Presenter {

            void SearchInvoice(String BranchId, String POId , String SuppId, String InvoiceNo, Config config);

            void searchItem(String POId , String ScanData, Config config);

            void AddData(String InvoiceId, String BranchId, String UserId, PurchaseProduct purchaseProduct, String qnt, Config config);
        }

        public interface View {

            void showProgress();

            void hideProgress();

            void onAddSuccessed(String id);

            void getInvoiceId(String id);

            void setProductDetails(PurchaseProduct product);

            void onFailure(String error);

            void onFailure(int error);
        }

    }


}
