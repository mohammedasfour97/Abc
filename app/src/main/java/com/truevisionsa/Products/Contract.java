package com.truevisionsa.Products;

import com.truevisionsa.ModelItems.Branch;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.InvProduct;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.ModelItems.Store;

import org.json.JSONObject;

import java.util.List;

public class Contract {

    public interface AddProducts {

        interface Model {

            interface OnFinishedListener {

                void onFinished(List<Product> productList);

                void onFinished(String result);

                void onFinished(JSONObject result);

                void onFailure(String error);
            }

            void getProducts(OnFinishedListener onFinishedListener, String branch_id, String company_id, String name, int scan_mode , boolean branches_only ,
                             Config config);

            void addProduct(OnFinishedListener onFinishedListener, String device_id, String ProductId, String StockId, String UnitsInPack,
                            String PacksQty, String UnitsQty, String StoreId, String BranchId, String userId, Config config);

            void addOnExistingInv(OnFinishedListener onFinishedListener , Config config , String units_in_pack , String pack_qn , String units_qn ,
                                  String store_id , String user_id , String inv_id , String old_packs_qn , String old_units_qn);

            void checkInv(OnFinishedListener onFinishedListener , String branch_id , String store_id , String stock_id , Config config);
        }

        interface Presenter {

            void requestProducts(String branch_id, String company_id, String name, int scan_mode , boolean branches_only , Config config);

            void requestAddProduct(String device_id, String ProductId, String StockId, String UnitsInPack, String PacksQty,
                                   String UnitsQty, String StoreId, String BranchId, String userId, Config config);

            void requestAddOnExistingInv(Config config , String units_in_pack , String pack_qn , String units_qn ,
                                         String store_id , String user_id , String inv_id , String old_packs_qn , String old_units_qn);

            void requestCheckInv(String branch_id , String store_id , String stock_id , Config config);
        }

        interface View {

            void fillRecyclerView(List<Product> result);

            void showAddDialog(String s);

            void showUpdateMessage(String OldPacksQty , String OldUnitsQty , String inv_id);

            void onFailure(int error);

            void onAddFibished();

            void showProgress(int progress);

            void hideProgress(int progress);
        }
    }

    public interface ViewProducts {

        interface Model {

            interface OnFinishedListener {

                void onFinished(List<InvProduct> productList);

                void onFinished(String result);

                void onFailure(String error);
            }

            void getInvProducts(OnFinishedListener onFinishedListener, String branch_id, String store_id, String name, boolean load_all , Config config);
            void editInventory(OnFinishedListener onFinishedListener , String UnitsInPack , String PacksQty, String UnitsQty , String StoreId ,
                               String UserId , String InventoryId , Config config);
            void deleteInventory(OnFinishedListener onFinishedListener , String inventory_id , Config config);

        }

        interface Presenter {

            void requestInvProducts(String branch_id, String store_id, String name, Config config);
            void requestEditInventory(String UnitsInPack , String PacksQty, String UnitsQty , String StoreId ,
                                     String UserId , String InventoryId , Config config);
            void requestDeleteInventory(String inventory_id , Config config);

        }

        interface View {

            void fillRecyclerView(List<InvProduct> result);

            void onEditFinished();

            void onDeleteFinished();

            void onFailure(int error);

            void showProgress();

            void hideProgress();
        }
    }
}

