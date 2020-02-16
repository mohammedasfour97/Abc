package com.truevisionsa.SalesOrderAndRelocateCheck;

import com.truevisionsa.ModelItems.CompareSaleItem;
import com.truevisionsa.ModelItems.CompareTransferItem;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Order;
import com.truevisionsa.ModelItems.SaleItem;
import com.truevisionsa.ModelItems.Transfer;
import com.truevisionsa.ModelItems.TransferItem;


import org.json.JSONObject;

import java.util.List;

public class Contract {

    public interface OrdersList{

        interface Model{

            interface OnFinishedListener {

                void onFinished (List<Order> orderList);

                void onFailure(String error);
            }

            void getOrderList(OnFinishedListener onFinishedListener , String OrderNo, String BranchId, String UserId, int LoadAll, Config config);

        }

        interface Presenter{

            void requestOrderList(String OrderNo, String BranchId, String UserId, int LoadAll, Config config);

        }

        interface View{

            void  fillRecyclerView(List<Order> orderList);

            void onFailure(int error);

            void showProgress();
            void hideProgress();
        }
    }

    public interface CheckItems{

        interface Model{

            interface OnFinishedListener {

                void onFinished (List<CompareSaleItem> compareSaleItemList);

                void _onFinished (List<SaleItem> saleItemList);

                void onFinish(JSONObject jsonObject);

                void onFailure(String error);
            }

            void getCompareItemList(OnFinishedListener onFinishedListener , String OrderNo, Config config);
            void getSaleItemList(OnFinishedListener onFinishedListener , String OrderNo, String count_mode , String product , Config config);
            void getSetOrder(OnFinishedListener onFinishedListener, String order_no , String user_id , Config config);

        }

        interface Presenter{

            void requestCompareItemList(String OrderNo, Config config);
            void requestSaleItemList(String OrderNo, String count_mode , String product , Config config);
            void requestSetOrder(String order_no , String user_id , Config config);

        }

        interface View{

            void  fillCompareItems(List<CompareSaleItem> compareSaleItemList);
            void  fillSaleItems(List<SaleItem> saleItemList);

            void onFinished();
            void onFailure(int error);

            void showProgress();
            void hideProgress();
        }
    }

    public interface TransferList{

        interface Model{

            interface OnFinishedListener {

                void onFinished (List<Transfer> transferList);

                void onFailure(String error);
            }

            void getTransferList(OnFinishedListener onFinishedListener , String BranchId, String dest_id, Config config);

        }

        interface Presenter{

            void requestTransferList(String BranchId, String dest_id, Config config);

        }

        interface View{

            void  fillRecyclerView(List<Transfer> transferList);

            void onFailure(int error);

            void showProgress();
            void hideProgress();
        }
    }


    public interface CheckTransferItems{

        interface Model{

            interface OnFinishedListener {

                void onFinished (List<CompareTransferItem> checkTransferItemsList);

                void _onFinished (List<TransferItem> transferItemList);

                void onFinish(JSONObject jsonObject);

                void onFailure(String error);
            }

            void getTransferCompareItemList(OnFinishedListener onFinishedListener , String Transfer_id, Config config);
            void getTransferItemList(OnFinishedListener onFinishedListener , String transfer_id, String count_mode , String product , Config config);
            void getSetTransfer(OnFinishedListener onFinishedListener, String transfer_id , String user_id , Config config);

        }

        interface Presenter{

            void requestTransferCompareItemList(String Transfer_id, Config config);
            void requestTransferItemList(String transfer_id, String count_mode , String product , Config config);
            void requestSetTransfer(String transfer_id , String user_id , Config config);

        }

        interface View{

            void  fillTransferCompareItems(List<CompareTransferItem> compareTransferItemList);
            void  fillTransferItems(List<TransferItem> saleItemList);

            void onFinished();
            void onFailure(int error);

            void showProgress();
            void hideProgress();
        }
    }
}
