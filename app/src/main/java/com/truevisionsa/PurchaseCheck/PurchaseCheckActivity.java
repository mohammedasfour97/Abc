package com.truevisionsa.PurchaseCheck;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.truevisionsa.BaseActivity;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.Fragments.PurchaseCheckFragment;
import com.truevisionsa.Fragments.PurchaseProductDetailsFragment;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.PurchaseProduct;
import com.truevisionsa.R;
import com.truevisionsa.Utils.TinyDB;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;


public class PurchaseCheckActivity extends BaseActivity implements Contract.PurchaseCheck.View {


    private EditText supp_id , invoice_num;
    private Button enter_inv_data;
    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;
    private TextView enter_inv_scanner;
    private PurchaseCheckPresenter purchaseCheckPresenter;
    private TinyDB tinyDB;
    private Config config;
    private String inv_id , sup_id , inv_no;
    private PurchaseCheckFragment purchaseCheckFragment;
    private PurchaseProductDetailsFragment purchaseProductDetailsFragment;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_check);

        initUI();
        setListener();

        initScanner();

        purchaseCheckPresenter = new PurchaseCheckPresenter(this , this);

        tinyDB = new TinyDB(this);

        config = new DatabaseHelper(this).getUser().get(0);
    }


    private void initUI(){

        supp_id = findViewById(R.id.supplier_id);
        invoice_num = findViewById(R.id.invoice_no);
        enter_inv_data = findViewById(R.id.enter_invoice_data);
        enter_inv_scanner = findViewById(R.id.enter_inv_title);
    }

    private void setListener(){

        enter_inv_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getSupportFragmentManager();

                purchaseCheckFragment = new PurchaseCheckFragment();
                purchaseCheckFragment.show(fm, "fragment_new_activity");
            }
        });


    }

    private void initScanner(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }

        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@androidx.annotation.NonNull final com.google.zxing.Result result) {

                PurchaseCheckActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        showProgress();
                        purchaseCheckPresenter.searchItem(result.getText().replace("\u001D" , "?"), config);

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


        mCodeScanner.setTouchFocusEnabled(false);
    }


    public void searchInvoiceNo(String supp_id , String inv_no){

        sup_id = supp_id; this.inv_no = inv_no;

        showProgress();
        purchaseCheckPresenter.SearchInvoice(tinyDB.getString("branch_id") , supp_id , inv_no , config);
    }


    public void addProduct(PurchaseProduct purchaseProduct , String qnt){

        showProgress();
        purchaseCheckPresenter.AddData(inv_id , tinyDB.getString("branch_id") , tinyDB.getString("user_id") , purchaseProduct , qnt , config);
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
    public void onAddSuccessed(String id) {

        Toast.makeText(this, getResources().getString(R.string.suc_add_item), Toast.LENGTH_SHORT).show();
        purchaseProductDetailsFragment.dismiss();
        mCodeScanner.startPreview();
    }

    @Override
    public void getInvoiceId(String id) {

        purchaseCheckFragment.dismiss();

        inv_id = id;
        supp_id.setText(sup_id);
        invoice_num.setText(inv_no);

        enter_inv_scanner.setVisibility(View.GONE);
        scannerView.setVisibility(View.VISIBLE);

         mCodeScanner.startPreview();
    }

    @Override
    public void setProductDetails(PurchaseProduct product) {

        FragmentManager fm = getSupportFragmentManager();

        purchaseProductDetailsFragment = new PurchaseProductDetailsFragment(product);
        purchaseProductDetailsFragment.show(fm, "fragment_new_activity");
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
    protected void onResume() {
        super.onResume();
        if(inv_id!=null)
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if(inv_id!=null)
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
