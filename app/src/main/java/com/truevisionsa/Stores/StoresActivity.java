package com.truevisionsa.Stores;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.Auth.LoginActivity;
import com.truevisionsa.BaseActivity;
import com.truevisionsa.DatabaseHelper;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.Store;
import com.truevisionsa.Products.Views.AddProductActivity;
import com.truevisionsa.R;
import com.truevisionsa.TinyDB;
import com.truevisionsa.UserPriviliges.UserPriviligesActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StoresActivity extends BaseActivity implements Contract.View {

    private RecyclerView recyclerView;
    private List<Store> itemsList;
    private StoresActivityAdapter mAdapter;
    private Contract.Presenter presenter ;
    private Config config ;
    private DatabaseHelper databaseHelper;
    private TinyDB tinyDB ;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        presenter = new StoresPresenter(this , this);

        databaseHelper = new DatabaseHelper(this);

        tinyDB = new TinyDB(this);

        initRecyclerView();

        initSwipeRefresh();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        requestStores();
    }


    private void initRecyclerView(){

        recyclerView = findViewById(R.id.recyclerview);

        itemsList = new ArrayList<>();
        mAdapter = new StoresActivityAdapter(StoresActivity.this, itemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(StoresActivity.this);
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

                requestStores();

            }
        });
    }


    private void requestStores(){

        showProgress();

        presenter.requestStores(tinyDB.getString("branch_id") , tinyDB.getString("user_id") , databaseHelper.getUser().get(0));
    }

    @Override
    public void fillRecyclerView(List<Store> result) {

        itemsList.clear();
        itemsList.addAll(result);

        mAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);

        Log.d("donedone", "yes");
    }

    @Override
    public void getUserPrivileges() {

       // presenter.requestStores(tinyDB.getString("branch_id") , tinyDB.getString("user_id") , databaseHelper.getUser().get(0));
        startActivity(new Intent(this , UserPriviligesActivity.class));
    }

    @Override
    public void onLogoutFinished() {

        startActivity(new Intent(this , LoginActivity.class));
        finish();
    }


    private void logout(){

        showProgress();

        String con_id  = tinyDB.getString("con_id");
        String user_id = tinyDB.getString("user_id");

        presenter.requestlogout(user_id , con_id , databaseHelper.getUser().get(0));
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



    public class StoresActivityAdapter extends RecyclerView.Adapter<StoresActivityAdapter.MyViewHolder> {
        private Context context;
        private List<Store> storeList;



        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView title , items_count;


            public MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.title);
                items_count = view.findViewById(R.id.items_count);
                context = itemView.getContext();


            }
        }

        public StoresActivityAdapter(Context context, List<Store> storeList) {
            this.context = context;
            this.storeList = storeList;
        }

        @Override
        public StoresActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.store_item, parent, false);

            return new StoresActivityAdapter.MyViewHolder(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final StoresActivityAdapter.MyViewHolder holder, final int position) {

            final Store store = storeList.get(position);

            holder.title.setText(store.getStore_name());

            holder.items_count.setText(store.getStore_items() + " " + getResources().getString(R.string.products));

            if (store.getStore_items().equals("0")) holder.title.setTextColor(Color.RED);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(StoresActivity.this , AddProductActivity.class);
                    intent.putExtra("store_id" , store.getStroe_id());
                    intent.putExtra("store_name" , store.getStore_name());

                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return storeList.size();
        }


    }
}
