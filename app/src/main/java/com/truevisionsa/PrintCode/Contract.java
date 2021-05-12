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
            void getAddedProducts(onFinishedListener onFinishedListener , String Branch_id ,String UserId, String ScanData, Boolean getall, Config config);
            void getModifyProduct(onFinishedListener onFinishedListener , String UserId, String Id, String Qty, Config config);
        }

        interface Presenter{

            void requestProductsBySearchText(String BranchId, String CompanyId, String ScanData,Config config);
            void requestProductsByBarCode(String BranchId, String CompanyId, String ScanData,Config config);
            void requestEnterQuantity(String BranchId, String UserId, String StockId, String Qty, Config config);
            void requestAddedProducts(String Branch_id , String UserId, String ScanData, Boolean getall, Config config);
            void requestModifyProduct(String UserId, String Id, String Qty, Config config);
        }

        interface View{

            void fillProducts(List productList);
            void onFinished(String s);
            void onFailure(String error);
            void onFailure(int error);

            void showRecyclerProgress();
            void hideRecyclerProgress();
            void showProgress();
            void hideProgress();
        }
    }
}
