package com.truevisionsa.DTTSScan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.truevisionsa.Auth.LoginActivity;
import com.truevisionsa.BaseActivity;
import com.truevisionsa.DTTSTransfer.DTTSDispatchTransferActivity;
import com.truevisionsa.DTTSTransfer.DTTSTransferPresenter;
import com.truevisionsa.Fragments.AddDTTSList;
import com.truevisionsa.Fragments.PurchaseProductDetailsFragment;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.DTTSList;
import com.truevisionsa.ModelItems.Transfer;
import com.truevisionsa.PurchaseCheck.PurchaseCheckActivity;
import com.truevisionsa.R;
import com.truevisionsa.Splash;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.Utils.TinyDB;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatSpinner;

public class DTTSScanActivity extends BaseActivity implements Contract.DTTSScan.View.MainView {

    private List<DTTSList> dttsLists;
    private DTTSScanPresenter DTTSTransferPresenter;
    private TinyDB tinyDB;
    private Config config;
    private ImageView back;
    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;
    private TextView item_count, select_list_tite, qr_code_txt;
    private Switch scanner_txtCode_switch;
    private Spinner lists_list;
    private LinearLayout add_list;
    private ArrayAdapter<DTTSList> dttsListArrayAdapter;
    private DTTSList selected_dtts_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtts_scan);

        initUI();
        setListners();
        initSpinners();
        initScanner();

        DTTSTransferPresenter = new DTTSScanPresenter(this , this);

        tinyDB = new TinyDB(this);

        config = new DatabaseHelper(this).getUser().get(0);

        requestDTTSTransferList();

    }


    private void initUI(){

        select_list_tite = findViewById(R.id.select_list_title);
        add_list = findViewById(R.id.add_list);
        item_count = findViewById(R.id.items_count);
        back = findViewById(R.id.back);
        lists_list = findViewById(R.id.lists);
        back= findViewById(R.id.back);
        scanner_txtCode_switch = findViewById(R.id.scanner_txt_code_switch);
        qr_code_txt = findViewById(R.id.scan_code_edt);

    }

    private void setListners(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AddDTTSList().show(getSupportFragmentManager(), "fragment_new_activity");
            }
        });

        scanner_txtCode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                qr_code_txt.setEnabled(b);

                if (b){

                    qr_code_txt.setBackground(getResources().getDrawable(R.drawable.white_solid_prim_stroke));
                    qr_code_txt.requestFocus();

                    scannerView.setVisibility(View.GONE);
                    select_list_tite.setVisibility(View.VISIBLE);

                    mCodeScanner.stopPreview();
                }

                else{

                    qr_code_txt.setBackground(getResources().getDrawable(R.drawable.white_solid_gray_stroke));
                    qr_code_txt.setText("");

                    select_list_tite.setVisibility(View.GONE);
                    scannerView.setVisibility(View.VISIBLE);

                    mCodeScanner.startPreview();
                }

            }
        });

//        qr_code_txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
//
//                    requestAddItem(qr_code_txt.getText().toString());
//
//                    return true;
//                }
//                return false;
//            }
//        });

        qr_code_txt.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                requestAddItem(qr_code_txt.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void initSpinners(){

        dttsLists = new ArrayList<>();

        dttsListArrayAdapter = new ArrayAdapter<DTTSList>(this, android.R.layout.simple_spinner_item, dttsLists);
        dttsListArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        lists_list.setAdapter(dttsListArrayAdapter);

        resetSpinners();
        setSpinnersListener();

    }

    private void resetSpinners(){

        dttsLists.clear();

        dttsLists.add(new DTTSList("-1", getResources().getString(R.string.choose_dtts_list), ""));

        dttsListArrayAdapter.notifyDataSetChanged();

    }

    private void setSpinnersListener() {

        lists_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_dtts_list = ((DTTSList) parent.getSelectedItem());

                if (selected_dtts_list.getId().equals("-1")){

                    scanner_txtCode_switch.setEnabled(false);
                    scanner_txtCode_switch.setChecked(false);

                    select_list_tite.setVisibility(View.VISIBLE);
                    scannerView.setVisibility(View.GONE);

                    item_count.setText("0");

                    if(mCodeScanner.isPreviewActive())
                        mCodeScanner.stopPreview();
                }

                else {

                    scanner_txtCode_switch.setEnabled(true);
                    scanner_txtCode_switch.setChecked(false);

                    requestAddedListCount();

                    select_list_tite.setVisibility(View.GONE);
                    scannerView.setVisibility(View.VISIBLE);

                    mCodeScanner.startPreview();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                DTTSScanActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        requestAddItem(result.getText().replace("\u001D" , "$"));

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


    private void requestAddItem(String result){

        showProgress();
        DTTSTransferPresenter.requestAddItem(result.replace("\u001D" , "?"),
                selected_dtts_list.getId(), tinyDB.getString("user_id"), config);
    }
    public void requestDTTSTransferList(){

        showProgress();
        DTTSTransferPresenter.requestLists(tinyDB.getString("branch_id") , tinyDB.getString("user_id") ,
                tinyDB.getString("id") , config);
    }

    private void requestAddedListCount(){

        showProgress();
        DTTSTransferPresenter.requestAddedListCount(selected_dtts_list.getId(), config);
    }

    @Override
    public void onFailure(int error) {

        Toast.makeText(this, getResources().getString(error), Toast.LENGTH_SHORT).show();

        qr_code_txt.setText("");

    }

    @Override
    public void fillListsList(List<DTTSList> listsList) {

        resetSpinners();

        dttsLists.addAll(listsList);

        dttsListArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListCountGot(String count) {

        item_count.setText(count);
    }

    @Override
    public void onItemAdded() {

        Toast.makeText(this, getResources().getString(R.string.succ_add_dtts_item), Toast.LENGTH_SHORT).show();

        mCodeScanner.startPreview();
        qr_code_txt.setText("");

        requestAddedListCount();
    }

    @Override
    public void onFailure(String error) {

        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

        qr_code_txt.setText("");
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
    protected void onResume() {
        super.onResume();
        if(selected_dtts_list != null && !selected_dtts_list.getId().equals("-1"))
            mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if(selected_dtts_list != null && !selected_dtts_list.getId().equals("-1"))
            mCodeScanner.releaseResources();
        super.onPause();
    }
}
