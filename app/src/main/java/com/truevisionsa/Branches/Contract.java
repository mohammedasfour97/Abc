package com.truevisionsa.Branches;

import com.truevisionsa.ModelItems.Branch;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Store;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Contract {

        public interface Model{

            interface OnFinishedListener {

                void onFinished(List<Branch> branchList);

                void onFinished(JSONObject result);

                void onFailure(String error);
            }

            void getEditDevice(OnFinishedListener onFinishedListener, String branch_id , String user_id ,
                               String device_id, Config config);
            void getBraches(OnFinishedListener onFinishedListener , Config config);
        }

        public interface Presenter{

            void requestEditDevice(String branch_id , String user_id , String device_id, Config config);
            void requestBranches(Config config);
        }

        public interface View{

            void fillRecyclerView(List<Branch> result);
            void onFinished();
            void onFailure(String error);
            void onFailure(int error);

        }
    }


