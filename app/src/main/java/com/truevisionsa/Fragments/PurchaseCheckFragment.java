package com.truevisionsa.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.truevisionsa.PurchaseCheck.PurchaseCheck.PurchaseCheckActivity;
import com.truevisionsa.R;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

public class PurchaseCheckFragment extends DialogFragment {

    private EditText sup_id, invoice_no;
    private ImageView close;
    private Button enter;
    private Toolbar toolbar;

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // safety check
            if (getDialog() == null)
                return;

            int dialogWidth = LinearLayout.LayoutParams.MATCH_PARENT;// specify a value here
            int dialogHeight = LinearLayout.LayoutParams.WRAP_CONTENT; // specify a value here

            getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

        }

        // ... other stuff you want to do in your onStart() method
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_purchase_check, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initUI(view);

        setListners();

        toolbar.setTitle(getResources().getString(R.string.enter_invoice_data));
    }


    private void initUI(View view) {

        close = view.findViewById(R.id.close);
        enter = view.findViewById(R.id.enter);
        sup_id = view.findViewById(R.id.supplier_id);
        invoice_no = view.findViewById(R.id.invoice_no);
        toolbar = view.findViewById(R.id.toolbar);

    }

    private void setListners() {

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getTexts()) ((PurchaseCheckActivity)getActivity()).searchInvoiceNo(sup_id.getText().toString() , invoice_no.getText().toString());
            }
        });
    }

    private boolean getTexts() {

        if (TextUtils.isEmpty(sup_id.getText())) {

            sup_id.setError(getResources().getString(R.string.enter_supplier_id));
            return false;
        } else if (TextUtils.isEmpty(invoice_no.getText())) {

            invoice_no.setError(getResources().getString(R.string.enter_purchase_invoice_no));
            return false;
        } else return true;

    }


}
