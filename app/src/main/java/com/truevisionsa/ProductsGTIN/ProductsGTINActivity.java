package com.truevisionsa.ProductsGTIN;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.BaseActivity;
import com.truevisionsa.DatabaseHelper;
import com.truevisionsa.Fragments.EditInvFragment;
import com.truevisionsa.Fragments.ProductDetailsFragment;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.InvProduct;
import com.truevisionsa.ModelItems.ProGTIN;
import com.truevisionsa.Products.Views.BarcodeActivity;
import com.truevisionsa.Products.Views.InvProductsActivity;
import com.truevisionsa.R;
import com.truevisionsa.Statics;
import com.truevisionsa.TinyDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsGTINActivity extends BaseActivity implements Contract.ProGTINList.View {

    private RecyclerView recyclerView;
    private ProductsGTINActivityAdapter mAdapter;
    private List<ProGTIN> gtinList;
    private ProductsGTINPresenter productsGTINPresenter;
    private TinyDB tinyDB;
    private Config config;
    private LinearLayout linearLayout;
    private TextView textView ;
    private String pro_id;
    private EditText searchView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_gtin);

        initUI();
        initInitialTextView();
        initRecyclerView();
        setListners();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        productsGTINPresenter = new ProductsGTINPresenter(this , this);

        tinyDB = new TinyDB(this);

        config = new DatabaseHelper(this).getUser().get(0);

        recyclerView.setVisibility(View.GONE);
        linearLayout.addView(textView);
    }


    private void initUI(){

        recyclerView = findViewById(R.id.recyclerview);
        linearLayout = findViewById(R.id.layout);
        searchView = findViewById(R.id.search);

    }


    private void initRecyclerView(){

        gtinList = new ArrayList<>();
        mAdapter = new ProductsGTINActivityAdapter(ProductsGTINActivity.this, gtinList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ProductsGTINActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }


    private void initInitialTextView(){

        textView = new TextView(this);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                RelativeLayout.LayoutParams.MATCH_PARENT); // Height of TextView

        lp.setMargins(10 , 10 , 10 , 10);
        // Apply the layout parameters to TextView widget
        textView.setLayoutParams(lp);

        textView.setText(getResources().getString(R.string.press_search));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP , 20);
        textView.setGravity(Gravity.CENTER);
    }


    private void setListners(){

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    requestProducts(searchView.getText().toString());
                    return true;
                }
                return false;
            }
        });

    }

    private void requestProducts(String name){

        showProgress();

        productsGTINPresenter.requestProGTINList(name , config);
    }


    private void requestAddGTIN(String scanned_data){

        showProgress();
        productsGTINPresenter.requestAddGTIN(tinyDB.getString("user_id") , pro_id , scanned_data , config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){

            requestAddGTIN(data.getStringExtra("result"));
        }
    }


    @Override
    public void onFinished(String m) {

        Toast.makeText(this, getResources().getString(R.string.gtin_updated_suc), Toast.LENGTH_SHORT).show();

        gtinList.clear();
        mAdapter.notifyDataSetChanged();
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

    @Override
    public void fillRecyclerView(List<ProGTIN> gtinList) {

        recyclerView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);

        this.gtinList.clear();

        this.gtinList.addAll(gtinList);

        mAdapter.notifyDataSetChanged();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public class ProductsGTINActivityAdapter extends RecyclerView.Adapter<ProductsGTINActivityAdapter.MyViewHolder> {
        private Context context;
        private List<ProGTIN> productList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView pro_id, pro_name, gtin_id;
            private ImageView img;


            public MyViewHolder(View view) {
                super(view);
                pro_id = view.findViewById(R.id.pro_id);
                pro_name = view.findViewById(R.id.pro_name);
                gtin_id = view.findViewById(R.id.gtin_id);
                img = view.findViewById(R.id.img);
                context = itemView.getContext();


            }
        }


        public ProductsGTINActivityAdapter(Context context, List<ProGTIN> productList) {
            this.context = context;
            this.productList = productList;
        }

        @Override
        public ProductsGTINActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progtin_item, parent, false);

            return new ProductsGTINActivityAdapter.MyViewHolder(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final ProductsGTINActivityAdapter.MyViewHolder holder, final int position) {

            final ProGTIN product = productList.get(position);

            holder.pro_id.setText(getResources().getString(R.string.product_id) + product.getProduct_id());

            holder.pro_name.setText(product.getProduct());

            holder.gtin_id.setText(getResources().getString(R.string.gtin) + product.getGtin());

            if (product.getValid_gtin().equals("true")) holder.img.setImageDrawable(getResources().getDrawable(R.drawable.t));

            else holder.img.setImageDrawable(getResources().getDrawable(R.drawable.f));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        pro_id = product.getProduct_id();

                        startActivityForResult(new Intent(ProductsGTINActivity.this , BarcodeActivity.class) , 1);
                }
            });
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
