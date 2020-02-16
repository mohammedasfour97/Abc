package com.truevisionsa.Products.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.BaseActivity;
import com.truevisionsa.DatabaseHelper;
import com.truevisionsa.Fragments.ProductDetailsFragment;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.Products.Presenters.AddProductPresenter;
import com.truevisionsa.Products.Contract;
import com.truevisionsa.R;
import com.truevisionsa.Statics;
import com.truevisionsa.TinyDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddProductActivity extends BaseActivity implements Contract.AddProducts.View {

    private RecyclerView recyclerView;
    private EditText pname ;
    private ImageView search , barcode;
    private List<Product> itemsList;
    private AddProductActivity.ProductsActivityAdapter mAdapter;
    private AddProductPresenter productPresenter;
    private TinyDB tinyDB ;
    private DatabaseHelper databaseHelper;
    private ProgressBar progressBar;
    public  String getProduct_name , getProduct_id, getStock_id , getExpiry , getBatch_no , getSale_price , getUnits_in_pack , getProduct_hidden ,
            old_packs_qn , old_units_qn , inven_id;
    private CheckBox auto_scan;
    private boolean sauto_scan;
    private static final int requestCode = 100;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        initUI();
        initRecyclerView();
        setListners();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(getIntent().getStringExtra("store_name"));

        tinyDB = new TinyDB(this);

        databaseHelper  = new DatabaseHelper(this);

        productPresenter = new AddProductPresenter(this , this);

        sauto_scan = false;
    }


    private void initUI(){

        pname = findViewById(R.id.name_serach);
        search = findViewById(R.id.search);
        barcode = findViewById(R.id.scan_barcode);
        progressBar = findViewById(R.id.progress);
        auto_scan = findViewById(R.id.auto_scan);
    }


    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recyclerview);

        itemsList = new ArrayList<>();
        mAdapter = new AddProductActivity.ProductsActivityAdapter(AddProductActivity.this, itemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddProductActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }


    private void setListners(){

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pname.getText().toString().isEmpty()){

                    pname.setError(getResources().getString(R.string.enter_product_name));
                    return;
                }

                requestProducts(pname.getText().toString() , 0);
            }
        });

        pname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    requestProducts(pname.getText().toString() , 0);
                    return true;
                }
                return false;
            }
        });

        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new Statics().checkCameraPermission(AddProductActivity.this,AddProductActivity.this,"Manifest.permission.CAMERA")) {

                    Intent i = new Intent(AddProductActivity.this, BarcodeActivity.class);
                    startActivityForResult(i, 1);

                }
            }
        });

        auto_scan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                sauto_scan = b;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){

            String barcode = data.getStringExtra("result");
            requestProducts(barcode , 1);
            pname.setText(barcode);
        }
    }


    private void requestProducts(String search_txt , int scan_mode){

        showProgress(0);

        productPresenter.requestProducts(tinyDB.getString("branch_id") , tinyDB.getString("company_id") , search_txt , scan_mode ,
                databaseHelper.getUser().get(0));
    }



    public void check_inv(String branch_id , String store_id , String stock_id){

        showProgress(1);

        productPresenter.requestCheckInv(branch_id , store_id , stock_id , databaseHelper.getUser().get(0));

    }

    @Override
    public void fillRecyclerView(List<Product> result) {

        itemsList.clear();
        itemsList.addAll(result);
        mAdapter.notifyDataSetChanged();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    @Override
    public void showAddDialog(String s) {

        FragmentManager fm = getSupportFragmentManager();

        ProductDetailsFragment productDetailsFragment = ProductDetailsFragment.newInstance
                (getProduct_name , getProduct_id , getStock_id , getExpiry , getBatch_no , getSale_price , getUnits_in_pack , getProduct_hidden ,
                        "yes" , "" , "" , s);
        productDetailsFragment.show(fm, "fragment_new_activity");
    }


    public void add_new_inv(String units_qn , String packs_qn) {

        showProgress(1);

        productPresenter.requestAddProduct(tinyDB.getString("id") , getProduct_id , getStock_id , getUnits_in_pack , packs_qn , units_qn ,
                getIntent().getStringExtra("store_id"), tinyDB.getString("branch_id") , tinyDB.getString("user_id") ,
                databaseHelper.getUser().get(0));

    }


    public void add_on_existing_inv(String pack_qn, String units_qn){

        showProgress(1);
        productPresenter.requestAddOnExistingInv(databaseHelper.getUser().get(0) , getUnits_in_pack , pack_qn , units_qn ,
                getIntent().getStringExtra("store_id") , tinyDB.getString("user_id"), inven_id , old_packs_qn , old_units_qn);

    }

    @Override
    public void showUpdateMessage(final String OldPacksQty, final String OldUnitsQty , final String inv_id) {

        old_packs_qn = OldPacksQty;
        old_units_qn = OldUnitsQty;
        inven_id = inv_id;

        new AlertDialog.Builder(this , R.style.MyAlertDialogStyle)
                .setTitle(getResources().getString(R.string.update_exist_inv_title))
                .setMessage(getResources().getString(R.string.update_exist_inv_body1) + " " + OldPacksQty + " " + getResources().getString(R.string.packs)
                + " and " + OldUnitsQty + " " + getResources().getString(R.string.units) + ". " + getResources().getString(R.string.update_exist_inv_body2))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        showAddDialog("no");

                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();

    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    */

    @Override
    public void onFailure(int error) {

        Toast.makeText(this, getResources().getString(error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddFibished() {

        Toast.makeText(this, getResources().getString(R.string.product_added_scucc), Toast.LENGTH_SHORT).show();

        pname.setText("");

        itemsList.clear();

        mAdapter.notifyDataSetChanged();

        if (sauto_scan){

            Intent i = new Intent(AddProductActivity.this, BarcodeActivity.class);
            startActivityForResult(i, 1);
        }
    }

    @Override
    public void showProgress(int progress) {

        if (progress == 0)
        progressBar.setVisibility(View.VISIBLE);
        else showProgressDialog();
    }

    @Override
    public void hideProgress(int progress) {
        if (progress == 0)
        progressBar.setVisibility(View.GONE);
        else hideProgressDialog();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.invproducts , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.invdata :

                Intent intent = new Intent(AddProductActivity.this , InvProductsActivity.class);
                intent.putExtra("store_id" , getIntent().getStringExtra("store_id"));
                startActivity(intent);
                break;

            case R.id.home :

                onBackPressed();
                break;
        }



        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public class ProductsActivityAdapter extends RecyclerView.Adapter<AddProductActivity.ProductsActivityAdapter.MyViewHolder> {
        private Context context;
        private List<Product> productList;



        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView pname , product_id , stock_id , expiry_date , batch_no ;
            private ImageView lock;


            public MyViewHolder(View view) {
                super(view);
                pname = view.findViewById(R.id.pname);
                product_id = view.findViewById(R.id.product_id);
                stock_id = view.findViewById(R.id.stoke_id);
                expiry_date = view.findViewById(R.id.expired_date);
                lock = view.findViewById(R.id.lock);
                batch_no = view.findViewById(R.id.batch_no);
                context = itemView.getContext();


            }
        }

        public ProductsActivityAdapter(Context context, List<Product> productList) {
            this.context = context;
            this.productList = productList;
        }

        @Override
        public AddProductActivity.ProductsActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_item, parent, false);

            return new AddProductActivity.ProductsActivityAdapter.MyViewHolder(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final AddProductActivity.ProductsActivityAdapter.MyViewHolder holder, final int position) {

            final Product product = productList.get(position);

            holder.pname.setText(product.getProduct_name());

            holder.product_id.setText(product.getProduct_id());

            holder.stock_id.setText(product.getStock_id());

            holder.batch_no.setText(product.getBatch_no());


         //   holder.expiry_date.setText(curFormater.format(Date.parse(product.getExpiry().substring(0 , 10))));

            holder.expiry_date.setText(product.getExpiry().substring(0 , 10));

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = new java.util.Date();
            long diff = 0;

        /*    Date date2 = null;
            try {
                date2 = df.parse(product.getExpiry());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            diff = date1.getTime() - date2.getTime();

            if (diff > 0) holder.pname.setTextColor(Color.RED);

           /* try {
                if (curFormater.parse(product.getExpiry()).before(new Date())) holder.pname.setTextColor(Color.RED);
            } catch (ParseException e) {
                e.printStackTrace();
            }
*/
        if (product.getItem_expired().equals("true")) holder.pname.setTextColor(Color.RED);


            if (product.getProduct_hidden().equals("true")){

                holder.lock.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    set_texts(product);
                    check_inv(tinyDB.getString("branch_id") , getIntent().getStringExtra("store_id") , product.getStock_id());
                }
            });

        }

        private void set_texts(Product product){

            getProduct_name = product.getProduct_name() ;
            getProduct_id = product.getProduct_id();  getStock_id  = product.getStock_id() ;
            getExpiry = product.getExpiry();
            getBatch_no = product.getBatch_no();
            getSale_price = product.getSale_price();
            getUnits_in_pack  =product.getUnits_in_pack();
            getProduct_hidden = product.getProduct_hidden();

        }

        @Override
        public int getItemCount() {
            return productList.size();
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
