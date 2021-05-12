package com.truevisionsa.DTTSTransfer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.BaseActivity;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.Products.Views.BarcodeActivity;
import com.truevisionsa.R;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.Utils.Statics;
import com.truevisionsa.Utils.TinyDB;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class DTTSDispatchTransferActivity extends BaseActivity implements Contract.DTTSTransfer.View {
    private LinearLayout scan_barcode;
    private RecyclerView recyclerView;
    private Contract.DTTSTransfer.Presenter presenter;
    private TinyDB tinyDB;
    private DatabaseHelper databaseHelper;
    private Config config;
    private ImageView back;
    private TextView dest_branch , transfer_id;
    private CheckBox auto_sacan;
    private List<Product> dttsTransferModelList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Bundle bundle;
    private String position;
    private DTTSDispatchTransferActivityAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtts_dispatch_transfer);

        initUI();
        setListners();
        initRecyclerView();
        initSwipeRefresh();
        initLabelTexts();

        tinyDB = new TinyDB(this);
        databaseHelper = new DatabaseHelper(this);
        config = databaseHelper.getUser().get(0);

        bundle = new Bundle();

        presenter = new DTTSTransferPresenter(this, this);

        requestDispatchTransferItems();
    }

    private void initUI() {

        scan_barcode = findViewById(R.id.scan_barcode);
        back = findViewById(R.id.back);
        transfer_id = findViewById(R.id.transfer_id);
        dest_branch = findViewById(R.id.dest_branch);
        auto_sacan = findViewById(R.id.auto_scan);
    }


    private void setListners() {

        scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bundle.putBoolean("come_from_scanner" , true);

                openScanner();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }


    private void initRecyclerView() {

        recyclerView = findViewById(R.id.recyclerview);

        dttsTransferModelList = new ArrayList<>();
        mAdapter = new DTTSDispatchTransferActivityAdapter(this, dttsTransferModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initSwipeRefresh(){

        swipeRefreshLayout= findViewById(R.id.swipeToRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                dttsTransferModelList.clear();
                requestDispatchTransferItems();

            }
        });
    }

    private void initLabelTexts(){

        dest_branch.setText(getIntent().getStringExtra("dest_branch"));
        transfer_id.setText(getIntent().getStringExtra("transfer_id"));
    }

    private void openScanner(){

        if (new Statics().checkCameraPermission(DTTSDispatchTransferActivity.this, DTTSDispatchTransferActivity.this,
                "Manifest.permission.CAMERA")) {

            Intent i = new Intent(DTTSDispatchTransferActivity.this, BarcodeActivity.class);
            startActivityForResult(i, 1);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {

            String barcode = data.getStringExtra("result");
            requestAddItem(barcode);
        }
    }

    private void requestDispatchTransferItems(){

        showProgress();
        presenter.requestAddedProducts(tinyDB.getString("branch_id") , getIntent().getStringExtra("transfer_id") , config);
    }


    private void requestAddItem(String search) {

        showProgress();

        presenter.requestAddItem(tinyDB.getString("branch_id"), getIntent().getStringExtra("transfer_id") , search ,
                tinyDB.getString("user_id"), config);
    }

    private void requestDeleteItem(String id) {

        showProgress();

        presenter.requestDeleteItem(id, config);
    }

    @Override
    public void onFinished(String m) {

        String message ;
        if (m.equals("add")) {
            message = getResources().getString(R.string.succ_add_item);

            if (auto_sacan.isChecked() && bundle.getBoolean("come_from_scanner"))
                openScanner();
        }

        else {
            message = getResources().getString(R.string.succ_delete_item);

            mAdapter.notifyItemRemoved(Integer.parseInt(position));
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        requestDispatchTransferItems();
    }

    @Override
    public void onFailure(int error) {

        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

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

        dttsTransferModelList.clear();
        dttsTransferModelList.addAll(list);
        mAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }


    public class DTTSDispatchTransferActivityAdapter extends RecyclerView.Adapter<DTTSDispatchTransferActivityAdapter.MyViewHolder> {
        private Context context;
        private List<Product> dttsTransferModelList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView pname, product_id, serial_no, stock_id , batch_no;
            private ImageView delete;


            public MyViewHolder(View view) {
                super(view);
                pname = view.findViewById(R.id.pname);
                product_id = view.findViewById(R.id.product_id);
                stock_id = view.findViewById(R.id.stoke_id);
                serial_no = view.findViewById(R.id.serial_no);
                delete = view.findViewById(R.id.delete);
                batch_no = view.findViewById(R.id.batch_no);
                context = itemView.getContext();

            }
        }

        public DTTSDispatchTransferActivityAdapter(Context context, List<Product> dttsTransferModelList) {
            this.context = context;
            this.dttsTransferModelList = dttsTransferModelList;
        }

        @Override
        public DTTSDispatchTransferActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dtts_dispatch_item, parent, false);

            return new DTTSDispatchTransferActivityAdapter.MyViewHolder(itemView);
        }

        public void onBindViewHolder(final DTTSDispatchTransferActivityAdapter.MyViewHolder holder, final int position) {

            final Product product = dttsTransferModelList.get(position);

            holder.pname.setText(product.getProduct_name());

            holder.product_id.setText(product.getProduct_id());

            holder.stock_id.setText(product.getStock_id());

            holder.serial_no.setText(product.getSerial_no());

            holder.batch_no.setText(product.getBatch_no());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(DTTSDispatchTransferActivity.this , R.style.MyAlertDialogStyle)
                            .setTitle(getResources().getString(R.string.delete_inv))
                            .setMessage(getResources().getString(R.string.sure_delete_item)  + " ?")
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    DTTSDispatchTransferActivity.this.position = String.valueOf(position);

                                    dttsTransferModelList.remove(position);

                                    requestDeleteItem(product.getId());
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.no), null)
                            .show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return dttsTransferModelList.size();
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
