package com.truevisionsa.SalesOrderAndRelocateCheck.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.truevisionsa.ModelItems.CompareTransferItem;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.TransferItem;
import com.truevisionsa.Products.Views.BarcodeActivity;
import com.truevisionsa.R;
import com.truevisionsa.SalesOrderAndRelocateCheck.Contract;
import com.truevisionsa.SalesOrderAndRelocateCheck.Presenters.CheckTransferItemsPresenter;
import com.truevisionsa.Utils.Statics;
import com.truevisionsa.Utils.TinyDB;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CheckTransferItemsActivity extends BaseActivity implements Contract.CheckTransferItems.View {

    private RadioGroup search_by;
    private RadioButton by_stock , by_product;
    private EditText search_name;
    private CheckBox auto_scan;
    private ImageView search_btn , back;
    private LinearLayout scan_barcode;
    private RecyclerView recyclerView;
    private Contract.CheckTransferItems.Presenter presenter;
    private TinyDB tinyDB;
    private DatabaseHelper databaseHelper;
    private Config config;
    private List<CompareTransferItem> compareTransferItemList;
    private List<TransferItem> transferItemList;
    private TransferItemsAdapter mAdapter;
    private Switch aSwitch;
    private TextView switch_status ,checkTv;

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

        compareTransferItemList = new ArrayList<>();

        presenter = new CheckTransferItemsPresenter(this , this);
        requestTransferCompareItems();
    }

    private void initUI(){

        search_by = findViewById(R.id.search_by);
        by_stock = findViewById(R.id.searc_by_stock);
        by_product = findViewById(R.id.searc_by_product);
        search_name = findViewById(R.id.name_serach);
        search_btn = findViewById(R.id.search);
        auto_scan = findViewById(R.id.auto_scan);
        scan_barcode = findViewById(R.id.scan_barcode);
        aSwitch = findViewById(R.id.count_mode);
        switch_status = findViewById(R.id.switch_status);
        back = findViewById(R.id.back);
        checkTv = findViewById(R.id.check_tv);
    }


    private void setListners(){

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (search_name.getText().toString().isEmpty()){

                    search_name.setError(getResources().getString(R.string.enter_product_name));
                    return;
                }

                requestTransferItems(search_name.getText().toString());
            }
        });

        search_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    requestTransferItems(search_name.getText().toString());
                    return true;
                }
                return false;
            }
        });

        scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new Statics().checkCameraPermission(CheckTransferItemsActivity.this, CheckTransferItemsActivity.this,"Manifest.permission.CAMERA")) {

                    Intent i = new Intent(CheckTransferItemsActivity.this, BarcodeActivity.class);
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
                }

                else {


                    switch_status.setText(getResources().getString(R.string.falsee));

                    switch_status.setTextColor(getResources().getColor(R.color.secondary_light));
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        checkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFullScan();
            }
        });
    }


    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recyclerview);

        transferItemList = new ArrayList<>();
        mAdapter = new TransferItemsAdapter(CheckTransferItemsActivity.this, transferItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CheckTransferItemsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){

            String barcode = data.getStringExtra("result");
            requestTransferItems(barcode);
            search_name.setText(barcode);
        }
    }


    private void requestTransferCompareItems(){

        showProgress();
        presenter.requestTransferCompareItemList(getIntent().getStringExtra("trans_id") , config);
    }


    private void requestTransferItems(String search){

        boolean search_mode;

        if (search_by.getCheckedRadioButtonId() == R.id.searc_by_product) search_mode = true;
        else search_mode = false;

        showProgress();

        presenter.requestTransferItemList(getIntent().getStringExtra("trans_id") , String.valueOf(search_mode), search , config);
    }
    @Override
    public void fillTransferCompareItems(List<CompareTransferItem> compareTransferItemList) {

        this.compareTransferItemList.addAll(compareTransferItemList);
    }

    @Override
    public void fillTransferItems(List<TransferItem> saleItemList) {

        FragmentManager fm = getSupportFragmentManager();

        SalesItemsListFragment salesItemsListFragment = new SalesItemsListFragment(saleItemList , aSwitch.isChecked() , "");
        salesItemsListFragment.show(fm, "fragment_new_activity");
    }

    public void fillCurrentList(TransferItem transferItem , int pack_qnt , int units_in_packs){

        TransferItem transferItem1;

        int a  , b = 0;

        for(a = 0 ; a < transferItemList.size(); a++){

            transferItem1 = transferItemList.get(a);

            if (transferItem1.getStock_id().equals(transferItem.getStock_id())){

                transferItem.setPacks_count(String.valueOf(Integer.parseInt(transferItem1.getPacks_count()) + pack_qnt));
                transferItem.setUnits_in_pack_count(String.valueOf(Integer.parseInt(transferItem1.getUnits_in_pack_count()) + units_in_packs));

                checkCompletedItem(transferItem , a , pack_qnt , units_in_packs);

                break;
            }
            b++;
        }

        if (b == transferItemList.size()) {

            transferItem.setPacks_count(String.valueOf(pack_qnt));
            transferItem.setUnits_in_pack_count(String.valueOf(units_in_packs));
            transferItemList.add(transferItem);
            checkCompletedItem(transferItem , transferItemList.indexOf(transferItem) , pack_qnt , units_in_packs);

        }

        mAdapter.notifyDataSetChanged();

        if (auto_scan.isChecked()){

            Intent i = new Intent(CheckTransferItemsActivity.this, BarcodeActivity.class);
            startActivityForResult(i, 1);
        }



    }


    private void checkCompletedItem(TransferItem transferItem , int a , int p , int u){

        for (CompareTransferItem compareTransferItem : compareTransferItemList){

            Log.d("poiu", transferItem.getStock_id() + " " + transferItem.getPacks_count() + " " + transferItem.getUnits_in_pack_count());
            if (transferItem.getStock_id().equals(compareTransferItem.getStock_id()) && transferItem.getPacks_count().equals(compareTransferItem.getPack_qnt())
            && transferItem.getUnits_in_pack_count().equals(compareTransferItem.getUnits_in_pack())){

                transferItem.setSign(true);

                break;
            }

            else if (transferItem.getStock_id().equals(compareTransferItem.getStock_id()) && Integer.parseInt(transferItem.getPacks_count()) >
                    Integer.parseInt(compareTransferItem.getPack_qnt())){

                onFailure(R.string.max_qnt);

                transferItem.setPacks_count(String.valueOf(Integer.parseInt(transferItem.getPacks_count()) - p));

                break;
            }

        }


        transferItemList.set(a , transferItem);

        mAdapter.notifyDataSetChanged();

    }


    private void checkFullScan(){

        int a = 0;

        for (CompareTransferItem compareTransferItem : compareTransferItemList){

            for (TransferItem transferItem : transferItemList){

                if (transferItem.getStock_id().equals(compareTransferItem.getStock_id()) && transferItem.getPacks_count()
                        .equals(compareTransferItem.getPack_qnt()) && transferItem.getUnits_in_pack_count().equals(compareTransferItem.getUnits_in_pack())){

                    a++;

                    break;
                }
            }
        }

        if (a == compareTransferItemList.size() && a!= 0){

           showProgress();

            presenter.requestSetTransfer(getIntent().getStringExtra("trans_id") , tinyDB.getString("user_id") , databaseHelper.getUser().get(0));

        }



        else Toast.makeText(this, getResources().getString(R.string.not_all_checked), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

            switch (menuItem.getItemId()){

                case R.id.check :
                    checkFullScan();
                    break;

                case android.R.id.home :
                    onBackPressed();
                    break;

            }

            return super.onOptionsItemSelected(menuItem);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok , menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onFinished() {

        Toast.makeText(this, getResources().getString(R.string.succ_check), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailure(int error) {

        Toast.makeText(this, getResources().getString(error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

        showProgressDialog();
    }

    @Override
    public void hideProgress() {

        hideProgressDialog();
    }


    public class TransferItemsAdapter extends RecyclerView.Adapter<TransferItemsAdapter.MyViewHolder> {
        private Context context;
        private List<TransferItem> transferItemList;



        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView pname , product_id , stock_id , expiry_date, batch, sales_price ;
            private ImageView lock , sign;
            private LinearLayout batch_layout;


            public MyViewHolder(View view) {
                super(view);
                pname = view.findViewById(R.id.pname);
                product_id = view.findViewById(R.id.product_id);
                stock_id = view.findViewById(R.id.stoke_id);
                expiry_date = view.findViewById(R.id.expired_date);
                batch = view.findViewById(R.id.batch_no);
                sales_price = view.findViewById(R.id.sale_price);
                lock = view.findViewById(R.id.lock);
                sign = view.findViewById(R.id.sign);
                context = itemView.getContext();


            }
        }


        public TransferItemsAdapter(Context context, List<TransferItem> transferItemList) {
            this.context = context;
            this.transferItemList = transferItemList;
        }

        @Override
        public TransferItemsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_item, parent, false);

            return new TransferItemsAdapter.MyViewHolder(itemView);
        }

        public void onBindViewHolder(final TransferItemsAdapter.MyViewHolder holder, final int position) {

            final TransferItem saleItem = transferItemList.get(position);

            holder.pname.setText(saleItem.getProduct());

            holder.expiry_date.setText(saleItem.getPacks_count() + " , " + saleItem.getUnits_in_pack_count());

            holder.stock_id.setText(saleItem.getStock_id());

//            holder.batch_layout.setVisibility(View.GONE);

            //   holder.expiry_date.setText(curFormater.format(Date.parse(product.getExpiry().substring(0 , 10))));

            holder.product_id.setText(saleItem.getExpiry().substring(0 , 10));

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

                if (df.parse(saleItem.getExpiry()).before(new Date())) holder.pname.setTextColor(Color.RED);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.batch.setText(saleItem.getBatch());
            holder.sales_price.setText(saleItem.getPack_value());

            holder.sign.setVisibility(View.VISIBLE);

            if (saleItem.isSign()) holder.sign.setImageDrawable(getResources().getDrawable(R.drawable.t));

        }

        @Override
        public int getItemCount() {
            return transferItemList.size();
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
