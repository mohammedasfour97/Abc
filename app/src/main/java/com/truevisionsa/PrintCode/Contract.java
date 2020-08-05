package com.truevisionsa.PrintCode;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Contract {

    public interface PrintCode {

        interface Model{

            interface onFinishedListener{

                void onFinished(JSONArray jsonArray);
                void onFinished(JSONObject jsonObject);
                void onFailure(String error);
            }

            void getProducts(onFinishedListener onFinishedListener , String BranchId, String CompanyId, String ScanData, String ScanMode, Config config);
            void getEnterQuantity(onFinishedListener onFinishedListener , String BranchId, String UserId, String StockId, String Qty, Config config);
            void getAddedProducts(onFinishedListener onFinishedListener , String UserId, String ScanData, String getall, Config config);
            void getModifyProduct(onFinishedListener onFinishedListener , String UserId, String Id, String Qty, Config config);
        }

        interface Presenter{

            void requestProducts(String BranchId, String CompanyId, String ScanData, String ScanMode, Config config);
            void requestEnterQuantity(String BranchId, String UserId, String StockId, String Qty, Config config);
            void requestAddedProducts(String UserId, String ScanData, String getall, Config config);
            void requestModifyProduct(String UserId, String Id, String Qty, Config config);
        }

        interface View{

            void fillProducts(List productList);
            void onFinished(String s);
            void onFailure(String error);
            void onFailure(int error);

            void showProgress();
            void hideProgress();
        }
    }
}
