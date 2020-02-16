package com.truevisionsa.SalesOrderAndRelocateCheck.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.BaseActivity;
import com.truevisionsa.DatabaseHelper;
import com.truevisionsa.ModelItems.Transfer;
import com.truevisionsa.R;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.SalesOrderAndRelocateCheck.Presenters.TransferListPresenter;
import com.truevisionsa.TinyDB;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class TransferListActivity extends BaseActivity implements Contract.TransferList.View {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private TransferListPresenter transferListPresenter;
    private TransferListActivityAdapter mAdapter;
    private TinyDB tinyDB;
    private DatabaseHelper databaseHelper;
    private List<Transfer> itemsList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        transferListPresenter = new TransferListPresenter(this , this);

        requestOrders("");
    }


    private void initUi(){

        recyclerView = findViewById(R.id.recyclerview);
    }


    private void setListners(){}


    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recyclerview);

        itemsList = new ArrayList<>();
        mAdapter = new TransferListActivity.TransferListActivityAdapter(TransferListActivity.this, itemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TransferListActivity.this);
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

                requestOrders("");

            }
        });
    }

    private void requestOrders(String txt){

        showProgress();

        transferListPresenter.requestTransferList(tinyDB.getString("branch_id") , "0" , databaseHelper.getUser().get(0));
    }


    @Override
    public void fillRecyclerView(List<Transfer> transferList) {

        itemsList.clear();
        itemsList.addAll(transferList);
        mAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(int error) {

        Toast.makeText(this, getResources().getString(error), Toast.LENGTH_SHORT).show();

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

    public class TransferListActivityAdapter extends RecyclerView.Adapter<TransferListActivity.TransferListActivityAdapter.MyViewHolder> {
        private Context context;
        private List<Transfer> transferList;


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


        public TransferListActivityAdapter(Context context, List<Transfer> transferList) {
            this.context = context;
            this.transferList = transferList;
        }

        @Override
        public TransferListActivity.TransferListActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dest_item, parent, false);

            return new TransferListActivity.TransferListActivityAdapter.MyViewHolder(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final TransferListActivity.TransferListActivityAdapter.MyViewHolder holder, final int position) {

            final Transfer transfer = transferList.get(position);

            holder.customer_id.setText(transfer.getDest_id());
            holder.order_id.setText(transfer.getDest_branch());
            holder.customer_name.setText(transfer.getTransfer_id());
            holder.items_count.setText(transfer.getItems() + " " + getResources().getString(R.string.items));
            holder.customer_id_title.setText(getResources().getString(R.string.dest_id));
            holder.customer_name_title.setText(getResources().getString(R.string.transfer_id));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(TransferListActivity.this , CheckTransferItemsActivity.class);
                    intent.putExtra("trans_id" , transfer.getTransfer_id());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return transferList.size();
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