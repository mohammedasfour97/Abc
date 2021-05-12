package com.truevisionsa.Products.Views;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.BaseActivity;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.Fragments.EditInvFragment;
import com.truevisionsa.Fragments.ProductDetailsFragment;
import com.truevisionsa.ModelItems.InvProduct;
import com.truevisionsa.Products.Contract;
import com.truevisionsa.Products.Presenters.InvProductsPresenter;
import com.truevisionsa.R;
import com.truevisionsa.Utils.Statics;
import com.truevisionsa.Utils.TinyDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class InvProductsActivity extends BaseActivity implements Contract.ViewProducts.View {

    private SearchView searchView;
    private List<InvProduct> itemsList;
    private InvProductsActivity.InvProductsActivityAdapter mAdapter;
    private RecyclerView recyclerView;
    private TinyDB tinyDB;
    private DatabaseHelper databaseHelper;
    private InvProductsPresenter invProductsPresenter;
    private String units_in_pack , inv_id ;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView back , scan;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invproducts);

        initUI();

        setListners();

        initRecyclerView();

        initSearchView();

        initSwipeRefresh();

        tinyDB = new TinyDB(this);

        databaseHelper = new DatabaseHelper(this);

        invProductsPresenter = new InvProductsPresenter(this , this);

        requestProducts("");
    }


    private void initUI(){

        back = findViewById(R.id.back);
        scan = findViewById(R.id.barcode);
    }

    private void setListners(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new Statics().checkCameraPermission(InvProductsActivity.this,InvProductsActivity.this,"Manifest.permission.CAMERA")) {

                    Intent i = new Intent(InvProductsActivity.this, BarcodeActivity.class);
                    startActivityForResult(i, 1);

                }

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

                requestProducts("");

            }
        });

    }

    private void initSearchView(){

        searchView = findViewById(R.id.action_search);
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
              if (query.isEmpty()) requestProducts("");
                return false;
            }
        });
        searchView.setIconified(false);
        searchView.clearFocus();

    }

    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recyclerview);

        itemsList = new ArrayList<>();
        mAdapter = new InvProductsActivity.InvProductsActivityAdapter(InvProductsActivity.this, itemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(InvProductsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void requestProducts(String search_txt){

        showProgress();

        invProductsPresenter.requestInvProducts(tinyDB.getString("branch_id") , getIntent().getStringExtra("store_id") , search_txt ,
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

            if (new Statics().checkCameraPermission(InvProductsActivity.this,InvProductsActivity.this,"Manifest.permission.CAMERA")) {

                Intent i = new Intent(InvProductsActivity.this, BarcodeActivity.class);
                startActivityForResult(i, 1);

            }

        }

        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void fillRecyclerView(List<InvProduct> result) {

        itemsList.clear();
        itemsList.addAll(result);
        mAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onEditFinished() {

        Toast.makeText(this, getResources().getString(R.string.succ_edit_inv), Toast.LENGTH_SHORT).show();

        searchView.setQuery("" , false);

        requestProducts("");
    }

    @Override
    public void onDeleteFinished() {

        Toast.makeText(this, getResources().getString(R.string.succ_delete_inv), Toast.LENGTH_SHORT).show();

        searchView.setQuery("" , false);

        requestProducts("");
    }


    public void editInvProduct(String packs_qn , String units_qn){

        showProgress();

        invProductsPresenter.requestEditInventory(units_in_pack , packs_qn , units_qn ,
                getIntent().getStringExtra("store_id") , tinyDB.getString("user_id") , inv_id ,
                databaseHelper.getUser().get(0));
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


   public class InvProductsActivityAdapter extends RecyclerView.Adapter<InvProductsActivity.InvProductsActivityAdapter.MyViewHolder>
   implements Filterable {
       private Context context;
       private List<InvProduct> productList;
       private List<InvProduct> filtered_productList;


       public class MyViewHolder extends RecyclerView.ViewHolder {
           Context context;
           private TextView pname, product_id, stock_id, expiry_date , qnt , batch_no , sale_price;
           private ImageView lock , edit , delete;
           private RelativeLayout edit_delete_layout;


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
               context = itemView.getContext();


           }
       }


       public InvProductsActivityAdapter(Context context, List<InvProduct> productList) {
           this.context = context;
           this.productList = productList;
           filtered_productList = productList ;
       }

       @Override
       public InvProductsActivity.InvProductsActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View itemView = LayoutInflater.from(parent.getContext())
                   .inflate(R.layout.inv_product_item, parent, false);

           return new InvProductsActivity.InvProductsActivityAdapter.MyViewHolder(itemView);
       }

       @RequiresApi(api = Build.VERSION_CODES.O)
       @Override
       public void onBindViewHolder(final InvProductsActivity.InvProductsActivityAdapter.MyViewHolder holder, final int position) {

           final InvProduct product = productList.get(position);

           holder.pname.setText(product.getProductName());

           holder.product_id.setText(product.getProductId());

           holder.stock_id.setText(product.getStockId());

           holder.batch_no.setText(product.getBatch_no());

           holder.sale_price.setText(product.getSalePrice());

           holder.qnt.setText(getResources().getString(R.string.packs_no) + " : " + product.getInvPacksQty() + " | " +
                   getResources().getString(R.string.uinpacks_qnt) + " : " + product.getInvUnitsQty());

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

           if (product.getItem_expired().equals("true")) holder.pname.setTextColor(Color.RED);

           if (product.getProductHidden().equals("true")){

               holder.lock.setVisibility(View.VISIBLE);
           }

           holder.edit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   FragmentManager fm = getSupportFragmentManager();

                   EditInvFragment editInvFragment = EditInvFragment.newInstance
                           (product.getUnitsInPack() , product.getProductName() , product.getInvPacksQty() , product.getInvUnitsQty());
                   editInvFragment.show(fm, "fragment_new_activity");

                   units_in_pack = product.getUnitsInPack();
                   inv_id = product.getInventoryId();
               }
           });

           holder.delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   new AlertDialog.Builder(InvProductsActivity.this , R.style.MyAlertDialogStyle)
                           .setTitle(getResources().getString(R.string.delete_inv))
                           .setMessage(getResources().getString(R.string.sure_delete_inv) + " " + product.getProductName() + " ?")
                           .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {

                                   showProgress();
                                   invProductsPresenter.requestDeleteInventory(product.getInventoryId() , databaseHelper.getUser().get(0));
                               }
                           })
                           .setNegativeButton(getResources().getString(R.string.no), null)
                           .show();

               }
           });

           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   FragmentManager fm = getSupportFragmentManager();

                   ProductDetailsFragment productDetailsFragment = ProductDetailsFragment.newInstance
                           (product.getProductName(), product.getProductId(), product.getStockId(), product.getExpiry(),
                                   "", product.getSalePrice(), product.getUnitsInPack(), product.getProductHidden() , "no" , product.getInvPacksQty()
                           , product.getInvUnitsQty() , "no");
                   productDetailsFragment.show(fm, "fragment_new_activity");
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
                       List<InvProduct> filteredList = new ArrayList<>();
                       for (InvProduct invProduct : productList) {

                           if (invProduct.getProductName().toLowerCase().contains(charString.toLowerCase())) {
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
                   filtered_productList = (ArrayList<InvProduct>) filterResults.values;
                   notifyDataSetChanged();
               }
           };

       }

   }
}
