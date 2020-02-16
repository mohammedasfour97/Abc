package com.truevisionsa.UserPriviliges;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Store;

import java.util.List;

public class Contract {

    public interface Model{

        interface OnFinishedListener {


            void onFinished(String result);

            void onFailure(String error);
        }



        void getPrivilige(OnFinishedListener onFinishedListener , String user_id , String screen_id ,  Config config);
        void getlogout(OnFinishedListener onFinishedListener , String user_id , String con_id , Config config);
    }

    public interface Presenter{


        void requestPrivilige(String user_id , String screen_id ,  Config config);
        void requestlogout(String user_id , String con_id , Config config);
    }

    public interface View{

        void onLogoutFinished();

        void onFinished();
        void onFailure(int error);

        void showProgress();
        void hideProgress();
    }
}

