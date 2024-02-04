package com.truevisionsa.DTTSScan;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.DTTSList;
import com.truevisionsa.ModelItems.IDName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Contract {

    public interface DTTSScan {

        interface Model{

            interface onFinishedListener{

                void onFinished(JSONArray jsonArray);
                void onFinished(JSONObject jsonObject);
                void onFailure(String error);
            }

            void getListTypes(onFinishedListener onFinishedListener ,Config config);
            void getLists(onFinishedListener onFinishedListener , String BranchId, String UserId, String DeviceId, Config config);
            void getAddList(onFinishedListener onFinishedListener , String Branch_id ,String DeviceId, String userId, String refId, String typeId,
                            Config config);
            void getAddedListCount(onFinishedListener onFinishedListener , String ListId, Config config);
            void getAddItem(onFinishedListener onFinishedListener , String ScanData, String ListId, String userId, Config config);

        }

        interface Presenter{

            void requestListTypes(Config config);
            void requestLists(String BranchId, String UserId, String DeviceId, Config config);
            void requestAddList(String Branch_id , String DeviceId, String userId, String RefId, String TypeId, Config config);
            void requestAddedListCount(String ListId, Config config);
            void requestAddItem(String ScanData, String ListId, String userId, Config config);
        }

        interface View{

            interface MainView{

                void fillListsList(List<DTTSList> listsList);

                void onListCountGot(String count);
                void onItemAdded();
                void onFailure(String error);
                void onFailure(int error);

                void showProgress();
                void hideProgress();
            }

            interface FragmentView{

                void fillListTypesList(List<IDName> listTypesList);
                void onListAdded();
                void onFailure(String error);
                void onFailure(int error);

                void showProgress();
                void hideProgress();
            }
        }

    }
}
