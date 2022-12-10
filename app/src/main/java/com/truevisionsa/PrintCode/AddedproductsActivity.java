package com.truevisionsa.PrintCode;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.ModelItems.Product;
import com.truevisionsa.Products.Views.BarcodeActivity;
import com.truevisionsa.R;
import com.truevisionsa.Utils.Statics;
import com.truevisionsa.Utils.TinyDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class AddedproductsActivity extends BaseActivity implements Contract.PrintCode.View {

    private SearchView searchView;
    private List<Product> itemsList;
    private AddedproductsActivity.AddedproductsActivityAdapter mAdapter;
    private RecyclerView recyclerView;
    private TinyDB tinyDB;
    private DatabaseHelper databaseHelper;
    private PrintCodePresenter printCodePresenter;
    private String units_in_pack , inv_id ;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView back;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        initUI();

        setListners();

        initRecyclerView();

        initSwipeRefresh();

        tinyDB = new TinyDB(this);

        databaseHelper = new DatabaseHelper(this);

        printCodePresenter = new PrintCodePresenter(this , this);

        requestAllProducts();
    }


    private void initUI(){

        back = findViewById(R.id.back);
    }

    private void setListners(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }

    private void initSwipeRefresh(){

        swipeRefreshLayout= findViewById(R.id.swipeToRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                itemsList.clear();

                mAdapter.notifyDataSetChanged();

                requestAllProducts();
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
                requestProducts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.isEmpty()) requestAllProducts();
                return false;
            }
        });

    }

    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recyclerview);

        itemsList = new ArrayList<>();
        mAdapter = new AddedproductsActivity.AddedproductsActivityAdapter(AddedproductsActivity.this, itemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(AddedproductsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void requestProducts(String search_txt){

        showProgress();

        printCodePresenter.requestAddedProducts(tinyDB.getString("branch_id") , tinyDB.getString("user_id") , search_txt, false,
                databaseHelper.getUser().get(0));
    }


    private void requestAllProducts(){

        showProgress();

        printCodePresenter.requestAddedProducts(tinyDB.getString("branch_id") , tinyDB.getString("user_id") , "", true,
                databaseHelper.getUser().get(0));

        hideKeyboard();
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

            if (new Statics().checkCameraPermission(AddedproductsActivity.this,AddedproductsActivity.this,
                    "Manifest.permission.CAMERA")) {

                Intent i = new Intent(AddedproductsActivity.this, BarcodeActivity.class);
                startActivityForResult(i, 1);

            }

        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void fillProducts(List productList) {

        itemsList.clear();
        itemsList.addAll(productList);
        mAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFinished(String s) {

        Toast.makeText(this, getResources().getString(R.string.suc_edited_qnt), Toast.LENGTH_SHORT).show();

        requestAllProducts();
    }

    @Override
    public void onFailure(String error) {

        Toast.makeText(this, error ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRecyclerProgress() {

       /////////
    }

    @Override
    public void hideRecyclerProgress() {

        /////////////
    }


    public void editQnt(final String qnt , final String id){

        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15 , 10 , 15 , 10);
        editText.setLayoutParams(layoutParams);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText(qnt);
      //  editText.setPadding(5 , 5 , 5 , 5);

        editText.requestFocus();
        editText.setFocusable(true);

        showKeyboard();

        int maxLengthofEditText = 5;
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthofEditText)});

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this , R.style.MyAlertDialogStyle);
        alertDialog.setView(editText);
        alertDialog.setCancelable(false);
        alertDialog.setTitle(R.string.enter_qn);
        alertDialog.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                showProgress();

                printCodePresenter.requestModifyProduct(tinyDB.getString("user_id") , id , editText.getText().toString() ,
                        databaseHelper.getUser().get(0));

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

            requestProducts(data.getStringExtra("result"));
        }
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

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }


    public class AddedproductsActivityAdapter extends RecyclerView.Adapter<AddedproductsActivity.AddedproductsActivityAdapter.MyViewHolder>
            implements Filterable {
        private Context context;
        private List<Product> productList;
        private List<Product> filtered_productList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView pname, product_id, stock_id, expiry_date , qnt , batch_no, sale_price, vat;
            private ImageView lock , edit , delete;
            private RelativeLayout edit_delete_layout;
            private LinearLayout vat_layout;


            public MyViewHolder(View view) {
                super(view);
                pname = view.findViewById(R.id.pname);
                product_id = view.findViewById(R.id.product_id);
                stock_id = view.findViewById(R.id.stoke_id);
                expiry_date = view.findViewById(R.id.expired_date);
                lock = view.findViewById(R.id.lock);
                edit_delete_layout = view.findViewById(R.id.edit_delete_layout);
                edit = view.findViewById(R.id.edit);
                delete = view.findViewById(R.id.delete);
                qnt = view.findViewById(R.id.qnt);
                batch_no = view.findViewById(R.id.batch_no);
                sale_price = view.findViewById(R.id.sale_price);
                vat = view.findViewById(R.id.vat);
                vat_layout = view.findViewById(R.id.vat_layout);
                context = itemView.getContext();


            }
        }


        public AddedproductsActivityAdapter(Context context, List<Product> productList) {
            this.context = context;
            this.productList = productList;
            filtered_productList = productList ;
        }

        @Override
        public AddedproductsActivity.AddedproductsActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inv_product_item, parent, false);

            return new AddedproductsActivity.AddedproductsActivityAdapter.MyViewHolder(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final AddedproductsActivity.AddedproductsActivityAdapter.MyViewHolder holder, final int position) {

            final Product product = productList.get(position);

            holder.pname.setText(product.getProduct_name());

            holder.product_id.setText(product.getProduct_id());

            holder.stock_id.setText(product.getStock_id());

            holder.batch_no.setText(product.getBatch_no());

            holder.qnt.setText(getResources().getString(R.string.packs_no) + " : " + product.getUnits_in_pack());

            holder.sale_price.setText(product.getSale_price());

            holder.vat_layout.setVisibility(View.VISIBLE);

            holder.vat.setText((Double.parseDouble(product.getVat()))*100 + "%");

            SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");

            //   holder.expiry_date.setText(curFormater.format(Date.parse(product.getExpiry().substring(0 , 10))));

            holder.expiry_date.setText(product.getExpiry().substring(0, 10));

    /*       try {
               if (curFormater.parse(product.getExpiry()).before(new Date()))
                   holder.pname.setTextColor(Color.RED);
           } catch (ParseException e) {
               e.printStackTrace();
           }
*/

            holder.delete.setVisibility(View.GONE);


            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    editQnt(product.getUnits_in_pack() , product.getId());
                }
            });

        }

        @Override
        public int getItemCount() {
            return filtered_productList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.equals("")){
                        filtered_productList = productList ;

                    }
                    else
                    {
                        List<Product> filteredList = new ArrayList<>();
                        for (Product invProduct : productList) {

                            if (invProduct.getProduct_name().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(invProduct);
                            }
                        }

                        filtered_productList = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filtered_productList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    filtered_productList = (ArrayList<Product>) filterResults.values;
                    notifyDataSetChanged();
                }
            };

        }

    }
}

