package com.truevisionsa.Stores;

import com.truevisionsa.ModelItems.Branch;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Store;

import java.util.ArrayList;
import java.util.List;

public class Contract {

    public interface Model{

        interface OnFinishedListener {

            void onFinished(List<Store> storeList);

            void onFinished(String result);

            void onFailure(String error);
        }

        void getStores(OnFinishedListener onFinishedListener , String branch_id , String user_id
                , Config config);
        void getlogout(OnFinishedListener onFinishedListener , String user_id , String con_id , Config config);
    }

    public interface Presenter{

        void requestStores( String branch_id , String user_id
                , Config config);
        void requestlogout(String user_id , String con_id , Config config);
    }

    public interface View{

        void fillRecyclerView(List<Store> result);

        void getUserPrivileges();
        void onLogoutFinished();
        void onFailure(int error);

        void showProgress();
        void hideProgress();
    }
}

