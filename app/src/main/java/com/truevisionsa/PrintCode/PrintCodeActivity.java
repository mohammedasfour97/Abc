package com.truevisionsa.PrintCode;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.BaseActivity;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.Products.Views.BarcodeActivity;
import com.truevisionsa.R;
import com.truevisionsa.Utils.TinyDB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PrintCodeActivity extends BaseActivity implements Contract.PrintCode.View {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout scan_barcode;
    private EditText search_edittext;
    private ImageView search , invdata , back;
    private PrintCodePresenter printCodePresenter;
    private Config config;
    private TinyDB tinyDB;
    private List<Product> itemsList;
    private PrintCodeActivityAdapter mAdapter;
    private boolean if_scan;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_code);

        initUI();
        setListener();
        initRecyclerView();

        config = new DatabaseHelper(this).getUser().get(0);

        tinyDB = new TinyDB(this);

        if_scan = false;

        printCodePresenter = new PrintCodePresenter(this , this);

    }

    private void initUI(){

        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progress);
        scan_barcode = findViewById(R.id.scan_barcode);
        search_edittext = findViewById(R.id.name_serach);
        search = findViewById(R.id.search);
        invdata = findViewById(R.id.invdata);
        back = findViewById(R.id.back);
    }


    private void setListener(){

        search_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (TextUtils.isEmpty(search_edittext.getText())) search_edittext.setError(getResources().getString(R.string.enter_product_name));
                    else searchProductsBySearchText(search_edittext.getText().toString());
                    return true;
                }
                return false;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(search_edittext.getText())){

                    search_edittext.setError(getResources().getString(R.string.enter_product_name));
                    hideKeyboard();
                }
                else searchProductsBySearchText(search_edittext.getText().toString());

            }
        });


        scan_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if_scan = true;

                Intent i = new Intent(PrintCodeActivity.this, BarcodeActivity.class);
                startActivityForResult(i, 1);
            }
        });

        invdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PrintCodeActivity.this , AddedproductsActivity.class);
                intent.putExtra("store_id" , getIntent().getStringExtra("store_id"));
                startActivity(intent);
            }
        });
    }


    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recyclerview);

        itemsList = new ArrayList<>();
        mAdapter = new PrintCodeActivityAdapter(PrintCodeActivity.this, itemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PrintCodeActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void searchProductsBySearchText(String s){

        showRecyclerProgress();

        printCodePresenter.requestProductsBySearchText(tinyDB.getString("branch_id") , tinyDB.getString("company_id") , s , config);

        hideKeyboard();
    }


    private void searchProductsByBarCode(String s){

        showRecyclerProgress();

        printCodePresenter.requestProductsByBarCode(tinyDB.getString("branch_id") , tinyDB.getString("company_id") , s , config);

        hideKeyboard();
    }


    private void sendQnt(final String stock_id){

        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15 , 10 , 15 , 10);
        editText.setLayoutParams(layoutParams);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        //editText.setPadding(5 , 5 , 5 , 5);

        int maxLengthofEditText = 5;
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthofEditText)});

        editText.requestFocus();
        editText.setFocusable(true);

        showKeyboard();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this , R.style.MyAlertDialogStyle);
        alertDialog.setView(editText);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(R.string.enter_qn);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                showProgress();

                printCodePresenter.requestEnterQuantity(tinyDB.getString("branch_id") , tinyDB.getString("user_id") , stock_id ,
                        editText.getText().toString() , config);

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1){

            searchProductsByBarCode(data.getStringExtra("result"));
        }
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

                Intent intent = new Intent(PrintCodeActivity.this , AddedproductsActivity.class);
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
    public void fillProducts(List productList) {

        itemsList.clear();
        itemsList.addAll(productList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinished(String s) {

        Toast.makeText(this, getResources().getString(R.string.suc_enter_qnt), Toast.LENGTH_SHORT).show();

        if (if_scan){

            Intent i = new Intent(PrintCodeActivity.this, BarcodeActivity.class);
            startActivityForResult(i, 1);

        }
    }

    @Override
    public void onFailure(String error) {

        Toast.makeText(this, error ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(int error) {

        Toast.makeText(this, getResources().getString(error) , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRecyclerProgress() {

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRecyclerProgress() {

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProgress() {

        showProgressDialog();
    }

    @Override
    public void hideProgress() {

        hideProgressDialog();
    }



    public class PrintCodeActivityAdapter extends RecyclerView.Adapter<PrintCodeActivityAdapter.MyViewHolder> {
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

        public PrintCodeActivityAdapter(Context context, List<Product> productList) {
            this.context = context;
            this.productList = productList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

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
            if (product.getExpiry().equals("true")) holder.pname.setTextColor(Color.RED);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sendQnt(product.getStock_id());
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
