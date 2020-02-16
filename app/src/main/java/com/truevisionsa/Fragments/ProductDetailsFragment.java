package com.truevisionsa.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.travijuu.numberpicker.library.NumberPicker;
import com.truevisionsa.Products.Views.AddProductActivity;
import com.truevisionsa.R;
import com.truevisionsa.TinyDB;

public class ProductDetailsFragment extends DialogFragment {

    private TextView pid  , toolbar_title , stoke_id , expired_date , batch_no , sale_price , units_in_pack , packs_qn , units_in_pack_qn ;
    private Toolbar toolbar;
    private NumberPicker no_in_pick ;
    private EditText pick_no ;
    private Button add_product ;
    private TinyDB tinyDB;
    private LinearLayout add_product_layout , batch_no_layout , packs_qn_layout , units_in_pack_qn_layout;

    public ProductDetailsFragment() {
    }

    public static ProductDetailsFragment newInstance(String product_name, String product_id, String stock_id, String expiry, String batch_no,
                                                     String sale_price, String units_in_pack, String product_hidden , String add , String pack_qn ,
                                                     String units_in_pack_qn , String neww) {

        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString("product_name", product_name);
        args.putString("product_id", product_id);
        args.putString("stock_id", stock_id);
        args.putString("expiry", expiry);
        args.putString("batch_no", batch_no);
        args.putString("sale_price", sale_price);
        args.putString("units_in_pack", units_in_pack);
        args.putString("product_hidden", product_hidden);
        args.putString("add", add);
        args.putString("pack_qn", pack_qn);
        args.putString("units_in_pack_qn", units_in_pack_qn);
        args.putString("new", neww);

        productDetailsFragment.setArguments(args);

        return productDetailsFragment;
    }

    @Override
    public void onStart()
    {
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
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initUI(view);

        setListners();

        set_text();


        tinyDB = new TinyDB(getContext());


        no_in_pick.setMax(Integer.parseInt(get_text("units_in_pack")) - 1);


        if (getArguments().getString("add").equals("no")){

            add_product_layout.setVisibility(View.GONE);
            batch_no_layout.setVisibility(View.GONE);
        }

        else {

            packs_qn_layout.setVisibility(View.GONE);
            units_in_pack_qn_layout.setVisibility(View.GONE);

            pick_no.requestFocus();
            pick_no.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }


     private void initUI(View view){

         pid = view.findViewById(R.id.pid);
         stoke_id = view.findViewById(R.id.stoke_id);
         expired_date = view.findViewById(R.id.expired_date);
         batch_no = view.findViewById(R.id.batch_no);
         sale_price = view.findViewById(R.id.sale_price);
         units_in_pack = view.findViewById(R.id.units_in_pack);
         toolbar = view.findViewById(R.id.toolbar);
         no_in_pick = view.findViewById(R.id.units_in_pack_no);
         pick_no = view.findViewById(R.id.packs_no);
         add_product = view.findViewById(R.id.add_product);
         add_product_layout = view.findViewById(R.id.add_product_layout);
         toolbar_title = view.findViewById(R.id.toolbar_title);
         batch_no_layout = view.findViewById(R.id.batch_no_layout);
         packs_qn = view.findViewById(R.id.packs_qnt);
         units_in_pack_qn = view.findViewById(R.id.units_in_pack_qnt);
         packs_qn_layout = view.findViewById(R.id.packs_qnt_layout);
         units_in_pack_qn_layout = view.findViewById(R.id.units_in_pack_qnt_layout);



     }


     private void setListners(){

         add_product.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if (pick_no.getText().toString().isEmpty()){

                     pick_no.setError(getResources().getString(R.string.enter_packs_qn));
                     return;
                 }

                 if (pick_no.getText().toString().equals("0") & String.valueOf(no_in_pick.getValue()).equals("0")){

                     Toast.makeText(getContext(), getResources().getString(R.string.no_packs_or_units), Toast.LENGTH_SHORT).show();
                     return;
                 }


                 if (get_text("new").equals("yes"))
                 ((AddProductActivity)getActivity()).add_new_inv(String.valueOf(no_in_pick.getValue()), pick_no.getText().toString());
                 else
                     ((AddProductActivity)getActivity()).add_on_existing_inv(String.valueOf(no_in_pick.getValue()), pick_no.getText().toString());

                 dismiss();
             }
         });
     }


     private String get_text (String arg){

        return getArguments().getString(arg);
     }


     private void set_text(){

         toolbar_title.setText(get_text("product_name"));
         pid.setText(get_text("product_id"));
         stoke_id.setText(get_text("stock_id"));
         expired_date.setText(get_text("expiry"));
         batch_no.setText(get_text("batch_no"));
         sale_price.setText(get_text("sale_price"));
         units_in_pack.setText(get_text("units_in_pack"));
         packs_qn.setText(get_text("pack_qn"));
         units_in_pack_qn.setText(get_text("units_in_pack_qn"));

     }
}