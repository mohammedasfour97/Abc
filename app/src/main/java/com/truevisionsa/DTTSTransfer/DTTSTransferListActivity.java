package com.truevisionsa.DTTSTransfer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.BaseActivity;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.ProGTIN;
import com.truevisionsa.ModelItems.Transfer;
import com.truevisionsa.Products.Views.BarcodeActivity;
import com.truevisionsa.Products.Views.InvProductsActivity;
import com.truevisionsa.R;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.Utils.TinyDB;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class DTTSTransferListActivity extends BaseActivity implements Contract.DTTSTransfer.View {

    private RecyclerView recyclerView;
    private DTTSTransferListActivityAdapter mAdapter;
    private List<Transfer> transferList;
    private DTTSTransferPresenter DTTSTransferPresenter;
    private TinyDB tinyDB;
    private Config config;
    private LinearLayout linearLayout;
    private TextView textView ;
    private ImageView back;
    private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        initUI();
        initRecyclerView();
        setListners();
        initSwipeRefresh();

        DTTSTransferPresenter = new DTTSTransferPresenter(this , this);

        tinyDB = new TinyDB(this);

        config = new DatabaseHelper(this).getUser().get(0);

        requestDTTSTransferList();

    }


    private void initUI(){

        recyclerView = findViewById(R.id.recyclerview);
        back = findViewById(R.id.back);

    }


    private void initRecyclerView(){

        transferList = new ArrayList<>();
        mAdapter = new DTTSTransferListActivityAdapter(DTTSTransferListActivity.this, transferList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DTTSTransferListActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }


    private void setListners(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }

    private void requestDTTSTransferList(){

        showProgress();
        DTTSTransferPresenter.requestDTTSTransferList(tinyDB.getString("branch_id") , "0" , tinyDB.getString("glnno") , config);
    }

    private void initSwipeRefresh(){

        swipeRefreshLayout= findViewById(R.id.swipeToRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                transferList.clear();

                mAdapter.notifyDataSetChanged();

                requestDTTSTransferList();

            }
        });
    }

    @Override
    public void onFinished(String m) {

        ///////////////////////
    }

    @Override
    public void onFailure(int error) {

        Toast.makeText(this, getResources().getString(error), Toast.LENGTH_SHORT).show();

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(String error) {

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

    @Override
    public void fillRecyclerView(List list) {

        transferList.clear();
        transferList.addAll(list);
        mAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }



    public class DTTSTransferListActivityAdapter extends RecyclerView.Adapter<DTTSTransferListActivityAdapter.MyViewHolder> {
        private Context context;
        private List<Transfer> transferList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView transferId , destId , destBranch , items ;
            private ImageView delete;

            public MyViewHolder(View view) {
                super(view);
                transferId = view.findViewById(R.id.transfer_id);
                destId = view.findViewById(R.id.dest_id);
                destBranch = view.findViewById(R.id.dest_branch);
                items = view.findViewById(R.id.items_count);
                delete = view.findViewById(R.id.delete);
                context = itemView.getContext();


            }
        }


        public DTTSTransferListActivityAdapter(Context context, List<Transfer> transferList) {
            this.context = context;
            this.transferList = transferList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dtts_transfer_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final Transfer transfer = transferList.get(position);

            holder.destBranch.setText(transfer.getDest_branch());
            holder.destId.setText(transfer.getDest_id());
            holder.items.setText(transfer.getItems() + " " + getResources().getString(R.string.items));
            holder.transferId.setText(transfer.getTransfer_id());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(DTTSTransferListActivity.this , DTTSDispatchTransferActivity.class);
                    intent.putExtra("transfer_id" , transfer.getTransfer_id());
                    intent.putExtra("dest_branch" , transfer.getDest_branch());
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
