package com.truevisionsa.ServerConfig;

import com.truevisionsa.ModelItems.Config;

import org.json.JSONObject;

public class Contract {

    public interface ServerConfig{

        interface Model{

            interface onFinishedListener{

                void onFinished(JSONObject result);
                void onFailure(Object error);
            }

            void checkCon(onFinishedListener onFinishedListener , Config config);
        }

        interface Presenter{

            void requestCheckCon(Config config);
        }

        interface View{

            void onFinished();
            void onFailure(int err);
            void onFailure(String err);

            void showProgress();
            void hideProgress();
        }
    }
}
