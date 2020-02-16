package com.truevisionsa.SalesOrderAndRelocateCheck.Views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.BaseActivity;
import com.truevisionsa.DatabaseHelper;
import com.truevisionsa.ModelItems.Order;
import com.truevisionsa.Products.Views.BarcodeActivity;
import com.truevisionsa.R;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.SalesOrderAndRelocateCheck.Presenters.OrdersPresenter;
import com.truevisionsa.Statics;
import com.truevisionsa.TinyDB;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class OrdersActivity extends BaseActivity implements Contract.OrdersList.View {


    private SearchView searchView;
    private RecyclerView recyclerView;
    private OrdersPresenter ordersPresenter;
    private TinyDB tinyDB;
    private DatabaseHelper databaseHelper;
    private List<Order> itemsList;
    private OrdersActivity.OrdersAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String query;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);


        initUi();
        setListners();
        initRecyclerView();
        initSwipeRefresh();

        tinyDB = new TinyDB(this);
        databaseHelper = new DatabaseHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ordersPresenter = new OrdersPresenter(this , this);

        query = "";

        requestOrders("");
    }


    private void initUi(){

        recyclerView = findViewById(R.id.recyclerview);
    }


    private void setListners(){}


    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recyclerview);

        itemsList = new ArrayList<>();
        mAdapter = new OrdersActivity.OrdersAdapter(OrdersActivity.this, itemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OrdersActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initSwipeRefresh(){

        swipeRefreshLayout= findViewById(R.id.swipeToRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                itemsList.clear();

                mAdapter.notifyDataSetChanged();

                requestOrders(query);

            }
        });
    }


    private void initSearchView(){

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setQueryHint(getResources().getString(R.string.search_products));
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                OrdersActivity.this.query = query;

                requestOrders(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                if (query.isEmpty()) requestOrders("");
                return false;
            }
        });

    }


    private void requestOrders(String txt){

        showProgress();

        if (txt.isEmpty()) ordersPresenter.requestOrderList("0" , tinyDB.getString("branch_id") , tinyDB.getString("user_id") , 1 ,
                databaseHelper.getUser().get(0));

        else ordersPresenter.requestOrderList(txt , tinyDB.getString("branch_id") , tinyDB.getString("user_id") , 0 ,
                databaseHelper.getUser().get(0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search , menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        initSearchView();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.barcode){

            if (new Statics().checkCameraPermission(OrdersActivity.this,OrdersActivity.this,"Manifest.permission.CAMERA")) {

                Intent i = new Intent(OrdersActivity.this, BarcodeActivity.class);
                startActivityForResult(i, 1);

            }

        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){

            query = data.getStringExtra("result");

            requestOrders(data.getStringExtra("result"));
        }
    }


    @Override
    public void fillRecyclerView(List<Order> orderList) {

        itemsList.clear();
        itemsList.addAll(orderList);
        mAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(int error) {

        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showProgress() {

        showProgressDialog();
    }

    @Override
    public void hideProgress() {

        hideProgressDialog();
    }


    public class OrdersAdapter extends RecyclerView.Adapter<OrdersActivity.OrdersAdapter.MyViewHolder> {
        private Context context;
        private List<Order> orderList;



        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView customer_id , customer_name , order_id , items_count , customer_id_title , customer_name_title;
            private ImageView lock;


            public MyViewHolder(View view) {
                super(view);
                customer_id = view.findViewById(R.id.customer_id);
                customer_name = view.findViewById(R.id.customer_name);
                order_id = view.findViewById(R.id.order_id);
                items_count = view.findViewById(R.id.items_count);
                customer_id_title = view.findViewById(R.id.customer_id_title);
                customer_name_title = view.findViewById(R.id.customer_name_title);
                context = itemView.getContext();


            }
        }

        public OrdersAdapter(Context context, List<Order> orderList) {
            this.context = context;
            this.orderList = orderList;
        }

        @Override
        public OrdersActivity.OrdersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_item, parent, false);

            return new OrdersActivity.OrdersAdapter.MyViewHolder(itemView);
        }

        public void onBindViewHolder(final OrdersActivity.OrdersAdapter.MyViewHolder holder, final int position) {

            final Order order = orderList.get(position);

            holder.customer_id.setText(order.getCustomer_id());
            holder.order_id.setText(order.getOrder_id());
            holder.customer_name.setText(order.getCustomer_name());
            holder.items_count.setText(order.getItems() + " " + getResources().getString(R.string.items));
            holder.customer_id_title.setText(getResources().getString(R.string.customer_id));
            holder.customer_name_title.setText(getResources().getString(R.string.customer_name));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(OrdersActivity.this , CheckSalesItemsActivity.class);
                    intent.putExtra("order_id" , order.getOrder_id());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
}
