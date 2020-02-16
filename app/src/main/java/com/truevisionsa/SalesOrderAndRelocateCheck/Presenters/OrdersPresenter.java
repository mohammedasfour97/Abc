package com.truevisionsa.SalesOrderAndRelocateCheck.Presenters;

import android.content.Context;

import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Order;
import com.truevisionsa.R;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.SalesOrderAndRelocateCheck.Models.OrdersModel;

import java.util.List;

public class OrdersPresenter implements Contract.OrdersList.Presenter {

    private Contract.OrdersList.Model model;
    private Contract.OrdersList.View view;
    private Context context;

    public OrdersPresenter(Contract.OrdersList.View view, Context context) {
        this.view = view;
        this.context = context;
        this.model = new OrdersModel(context);
    }

    @Override
    public void requestOrderList(String OrderNo, String BranchId, String UserId, int LoadAll, Config config) {

        model.getOrderList(new Contract.OrdersList.Model.OnFinishedListener() {
            @Override
            public void onFinished(List<Order> orderList) {

                if (orderList.size() == 0) onFailure("empty");

                else {
                    view.hideProgress();
                    view.fillRecyclerView(orderList);
                }
            }

            @Override
            public void onFailure(String error) {

                view.hideProgress();

                if (error.equals("empty")) view.onFailure(R.string.no_orders);
                else view.onFailure(R.string.error_req);
            }
        } , OrderNo , BranchId , UserId , LoadAll , config);
    }
}
