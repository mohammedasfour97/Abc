package com.truevisionsa.SalesOrderAndRelocateCheck.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.Fragments.SalesItemsListFragment;
import com.truevisionsa.ModelItems.CompareSaleItem;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.SaleItem;
import com.truevisionsa.Products.Views.BarcodeActivity;
import com.truevisionsa.R;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.SalesOrderAndRelocateCheck.Presenters.CheckSalesItemsPresenter;
import com.truevisionsa.Utils.Statics;
import com.truevisionsa.Utils.TinyDB;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckSalesItemsActivity extends BaseActivity implements Contract.CheckItems.View {


    private RadioGroup search_by;
    private RadioButton by_stock, by_product;
    private EditText search_name;
    private CheckBox auto_scan;
    private ImageView search_btn;
    private LinearLayout scan_barcode;
    private RecyclerView recyclerView;
    private Contract.CheckItems.Presenter presenter;
    private TinyDB tinyDB;
    private DatabaseHelper databaseHelper;
    private Config config;
    private List<CompareSaleItem> compareSaleItems;
    private List<SaleItem> itemsList;
    private CheckItemsActivityAdapter mAdapter;
    private Switch aSwitch;
    private TextView switch_status, checkTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_items);

        initUI();
        setListners();
        initRecyclerView();

        tinyDB = new TinyDB(this);
        databaseHelper = new DatabaseHelper(this);
        config = databaseHelper.getUser().get(0);

        compareSaleItems = new ArrayList<>();

        presenter = new CheckSalesItemsPresenter(this, this);
        requestCompareItems();
    }

    private void initUI() {

        search_by = findViewById(R.id.search_by);
        by_stock = findViewById(R.id.searc_by_stock);
        by_product = findViewById(R.id.searc_by_product);
        search_name = findViewById(R.id.name_serach);
        search_btn = findViewById(R.id.search);
        auto_scan = findViewById(R.id.auto_scan);
        scan_barcode = findViewById(R.id.scan_barcode);
        aSwitch = findViewById(R.id.count_mode);
        switch_status = findViewById(R.id.switch_status);
        checkTv = findViewById(R.id.check_tv);

    }


    private void setListners() {

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (search_name.getText().toString().isEmpty()) {

                    search_name.setError(getResources().getString(R.string.enter_product_name));
                    return;
                }

                requestSalesItems(search_name.getText().toString());
            }
        });

        search_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    requestSalesItems(search_name.getText().toString());
                    return true;
                }
                return false;
            }
        });

        scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new Statics().checkCameraPermission(CheckSalesItemsActivity.this, CheckSalesItemsActivity.this,
                        "Manifest.permission.CAMERA")) {

                    Intent i = new Intent(CheckSalesItemsActivity.this, BarcodeActivity.class);
                    startActivityForResult(i, 1);

                }
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {

                    switch_status.setText(getResources().getString(R.string.truee));

                    switch_status.setTextColor(getResources().getColor(R.color.like_icon_activated));
                } else {


                    switch_status.setText(getResources().getString(R.string.falsee));

                    switch_status.setTextColor(getResources().getColor(R.color.secondary_light));
                }

            }
        });

        checkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFullScan();
            }
        });
    }


    private void initRecyclerView() {

        recyclerView = findViewById(R.id.recyclerview);

        itemsList = new ArrayList<>();
        mAdapter = new CheckItemsActivityAdapter(CheckSalesItemsActivity.this, itemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CheckSalesItemsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {

            String barcode = data.getStringExtra("result");
            requestSalesItems(barcode);
            search_name.setText(barcode);
        }
    }


    private void requestCompareItems() {

        showProgress();
        presenter.requestCompareItemList(getIntent().getStringExtra("order_id"), config);
    }


    private void requestSalesItems(String search) {

        boolean search_mode;

        if (search_by.getCheckedRadioButtonId() == R.id.searc_by_product) search_mode = true;
        else search_mode = false;

        showProgress();

        presenter.requestSaleItemList(getIntent().getStringExtra("order_id"), String.valueOf(search_mode), search, config);
    }

    @Override
    public void fillCompareItems(List<CompareSaleItem> compareSaleItemList) {

        compareSaleItems.addAll(compareSaleItemList);
    }

    @Override
    public void fillSaleItems(List<SaleItem> saleItemList) {

        FragmentManager fm = getSupportFragmentManager();

        SalesItemsListFragment salesItemsListFragment = new SalesItemsListFragment(saleItemList, aSwitch.isChecked());
        salesItemsListFragment.show(fm, "fragment_new_activity");

    }


    public void fillCurrentList(SaleItem saleItem, int qnt) {

        SaleItem saleItem1;

        int a, b = 0;

        for (a = 0; a < itemsList.size(); a++) {

            saleItem1 = itemsList.get(a);

            if (saleItem1.getStock_id().equals(saleItem.getStock_id())) {

                saleItem.setCount(String.valueOf(Integer.parseInt(saleItem1.getCount()) + qnt));

                checkCompletedItem(saleItem, a, qnt);

                break;
            }
            b++;
        }

        if (b == itemsList.size()) {

            saleItem.setCount(String.valueOf(qnt));
            itemsList.add(saleItem);
            checkCompletedItem(saleItem, itemsList.indexOf(saleItem), qnt);

        }

        mAdapter.notifyDataSetChanged();

        if (auto_scan.isChecked()) {

            Intent i = new Intent(CheckSalesItemsActivity.this, BarcodeActivity.class);
            startActivityForResult(i, 1);
        }


    }


    private void checkCompletedItem(SaleItem saleItem, int a, int p) {

        for (CompareSaleItem compareSaleItem : compareSaleItems) {

            if (saleItem.getStock_id().equals(compareSaleItem.getStock_id()) && saleItem.getCount().equals(compareSaleItem.getPack_qnt())) {

                saleItem.setSign(true);


                break;
            } else if (saleItem.getStock_id().equals(compareSaleItem.getStock_id()) && Integer.parseInt(saleItem.getCount()) >
                    Integer.parseInt(compareSaleItem.getPack_qnt())) {

                onFailure(R.string.max_qnt);

                saleItem.setCount(String.valueOf(Integer.parseInt(saleItem.getCount()) - p));

                break;
            }

        }


        itemsList.set(a, saleItem);

        mAdapter.notifyDataSetChanged();

    }


    private void checkFullScan() {

        int a = 0;

        for (CompareSaleItem compareSaleItem : compareSaleItems) {

            for (SaleItem saleItem : itemsList) {

                if (saleItem.getStock_id().equals(compareSaleItem.getStock_id()) && saleItem.getCount().equals(compareSaleItem.getPack_qnt())) {

                    a++;

                    break;
                }
            }
        }

        if (a == compareSaleItems.size() && a != 0) {

            showProgress();

            presenter.requestSetOrder(getIntent().getStringExtra("order_id"), tinyDB.getString("user_id"), databaseHelper.getUser().get(0));

        } else
            Toast.makeText(this, getResources().getString(R.string.not_all_checked), Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.check:
                checkFullScan();
                break;

            case android.R.id.home:
                onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFinished() {

        Toast.makeText(this, getResources().getString(R.string.succ_check), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void onFailure(int error) {

        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

        showProgressDialog();
    }

    @Override
    public void hideProgress() {

        hideProgressDialog();
    }


    public class CheckItemsActivityAdapter extends RecyclerView.Adapter<CheckSalesItemsActivity.CheckItemsActivityAdapter.MyViewHolder> {
        private Context context;
        private List<SaleItem> saleItemslist;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView pname, product_id, stock_id, expiry_date;
            private ImageView lock, sign;
            private LinearLayout batch_layout;


            public MyViewHolder(View view) {
                super(view);
                pname = view.findViewById(R.id.pname);
                product_id = view.findViewById(R.id.product_id);
                stock_id = view.findViewById(R.id.stoke_id);
                expiry_date = view.findViewById(R.id.expired_date);
                lock = view.findViewById(R.id.lock);
                sign = view.findViewById(R.id.sign);
                context = itemView.getContext();


            }
        }


        public CheckItemsActivityAdapter(Context context, List<SaleItem> saleItemList) {
            this.context = context;
            this.saleItemslist = saleItemList;
        }

        @Override
        public CheckItemsActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_item, parent, false);

            return new CheckItemsActivityAdapter.MyViewHolder(itemView);
        }

        public void onBindViewHolder(final CheckItemsActivityAdapter.MyViewHolder holder, final int position) {

            final SaleItem saleItem = saleItemslist.get(position);

            holder.pname.setText(saleItem.getProduct());

            holder.expiry_date.setText(saleItem.getCount() + " " + getResources().getString(R.string.packs));

            holder.stock_id.setText(saleItem.getStock_id());

            holder.batch_layout.setVisibility(View.GONE);


            //   holder.expiry_date.setText(curFormater.format(Date.parse(product.getExpiry().substring(0 , 10))));

            holder.product_id.setText(saleItem.getExpiry().substring(0, 10));

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        /*    Date date1 = new java.util.Date();
            long diff = 0;

            Date date2 = null;
            try {
                date2 = df.parse(saleItem.getExpiry());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            diff = date1.getTime() - date2.getTime();

            if (diff > 0) holder.pname.setTextColor(Color.RED);
*/
            try {

                if (df.parse(saleItem.getExpiry()).before(new Date()))
                    holder.pname.setTextColor(Color.RED);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.sign.setVisibility(View.VISIBLE);

            if (saleItem.isSign())
                holder.sign.setImageDrawable(getResources().getDrawable(R.drawable.t));

        }

        @Override
        public int getItemCount() {
            return saleItemslist.size();
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
