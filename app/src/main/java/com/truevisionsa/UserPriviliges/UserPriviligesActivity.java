package com.truevisionsa.UserPriviliges;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.Auth.LoginActivity;
import com.truevisionsa.BaseActivity;
import com.truevisionsa.DTTSTransfer.DTTSTransferListActivity;
import com.truevisionsa.PurchaseCheck.PurchaseOrderCheck.PurchaseOrderCheckActivity;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.Fragments.BranchesListFragment;
import com.truevisionsa.PrintCode.PrintCodeActivity;
import com.truevisionsa.ProductsGTIN.ProductsGTINActivity;
import com.truevisionsa.PurchaseCheck.PurchaseCheck.PurchaseCheckActivity;
import com.truevisionsa.R;
import com.truevisionsa.SalesOrderAndRelocateCheck.Views.OrdersActivity;
import com.truevisionsa.SalesOrderAndRelocateCheck.Views.TransferListActivity;
import com.truevisionsa.Stores.StoresActivity;
import com.truevisionsa.Utils.TinyDB;

import androidx.annotation.Nullable;

public class UserPriviligesActivity extends BaseActivity implements Contract.View {

    private LinearLayout inv_check , relocate_check , transfer_delivery , sales_order_check , sales_invoice_delivery , customer_receipts , reports ,
    products_gtin , check_purchase , print_barcode , purchase_order_check , dtts;
    private int priv_id;
    private UserPriviligesPresenter priviligesPresenter;
    private DatabaseHelper databaseHelper;
    private TinyDB tinyDB;
    public TextView branch_tile;
    private ImageView menu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_priviliges);

        initUI();

        priviligesPresenter = new UserPriviligesPresenter(this , this);

        setListners();

        databaseHelper = new DatabaseHelper(this);
        tinyDB= new TinyDB(this);

        getBranches();
    }


    private void initUI(){

        inv_check = findViewById(R.id.inv_check);
        relocate_check = findViewById(R.id.relocate_check);
        transfer_delivery = findViewById(R.id.transfer_delivery);
        sales_order_check = findViewById(R.id.sales_order_check);
        sales_invoice_delivery = findViewById(R.id.sales_invoice_delivery);
        customer_receipts = findViewById(R.id.customer_receipts);
        reports = findViewById(R.id.reports);
        branch_tile = findViewById(R.id.branch_title);
        products_gtin = findViewById(R.id.products_gtin);
        check_purchase = findViewById(R.id.check_purchase);
        print_barcode = findViewById(R.id.print_barcode);
        purchase_order_check = findViewById(R.id.purchase_order_check);
        dtts = findViewById(R.id.dtts_dispatch_transfer);
        menu = findViewById(R.id.menu);
    }


    private void setListners(){

        inv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 0;
                getPrivilege();
            }
        });

        relocate_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 1;
                getPrivilege();
            }
        });

        transfer_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 2;
                getPrivilege();
            }
        });

        sales_order_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 3;
                getPrivilege();
            }
        });

        sales_invoice_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 4;
                getPrivilege();
            }
        });

        customer_receipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 5;
                getPrivilege();
            }
        });

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 6;
                getPrivilege();
            }
        });

        products_gtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 7;
                getPrivilege();
            }
        });

        check_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 8;
                getPrivilege();
            }
        });

        print_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 9;
                getPrivilege();
            }
        });
        print_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 9;
                getPrivilege();
            }
        });
        purchase_order_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                priv_id = 10;
                getPrivilege();
            }
        });
        dtts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Boolean.parseBoolean(tinyDB.getString("dtts")) && !tinyDB.getString("glnno").isEmpty()) {
                    priv_id = 11;
                    getPrivilege();
                }
                else {

                    Toast.makeText(UserPriviligesActivity.this, getResources().getString(R.string.cannot_open_dtts), Toast.LENGTH_SHORT).show();
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(UserPriviligesActivity.this, menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.stores_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){

                            case R.id.branches :
                                getBranches();
                                break;


                            case R.id.logout :
                                logout();
                                break;
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }

        });
    }


    private void getBranches(){

        FragmentManager fm = getSupportFragmentManager();
        BranchesListFragment branchesListFragment = BranchesListFragment.newInstance();
        branchesListFragment.show(fm, "fragment_new_activity");
    }


    private void getPrivilege(){

        showProgress();

        priviligesPresenter.requestPrivilige(new TinyDB(this).getString("user_id") , String.valueOf(priv_id)
                ,new DatabaseHelper(this).getUser().get(0));
    }

    @Override
    public void onLogoutFinished() {

        startActivity(new Intent(this , LoginActivity.class));
        finish();
    }

    @Override
    public void onFinished() {

        switch (priv_id){

            case 0 :
                startActivity(new Intent(UserPriviligesActivity.this , StoresActivity.class));
                break;

            case 1 :
                startActivity(new Intent(UserPriviligesActivity.this , TransferListActivity.class));
                break;

            case 3 :
                startActivity(new Intent(UserPriviligesActivity.this , OrdersActivity.class));
                break;

            case 4 :
                break;

            case 7 :
                startActivity(new Intent(UserPriviligesActivity.this , ProductsGTINActivity.class));
                break;

            case 8 :
                startActivity(new Intent(UserPriviligesActivity.this , PurchaseCheckActivity.class));
                break;

            case 9 :
                startActivity(new Intent(UserPriviligesActivity.this , PrintCodeActivity.class));
                break;
            case 10 :
                startActivity(new Intent(UserPriviligesActivity.this , PurchaseOrderCheckActivity.class));
                break;
            case 11 :
                startActivity(new Intent(UserPriviligesActivity.this , DTTSTransferListActivity.class));
                break;
        }
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
    public void onBackPressed() {

    }

    private void logout(){

        showProgress();

        String con_id  = tinyDB.getString("con_id");
        String user_id = tinyDB.getString("user_id");

        priviligesPresenter.requestlogout(user_id , con_id , databaseHelper.getUser().get(0));
    }
}
