package com.truevisionsa.Fragments;

import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.truevisionsa.ModelItems.PurchaseProduct;
import com.truevisionsa.PurchaseCheck.PurchaseCheck.PurchaseCheckActivity;
import com.truevisionsa.R;
import com.truevisionsa.Utils.InputFilterMinMax;
import com.truevisionsa.Utils.TinyDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

public class PurchaseProductDetailsFragment extends DialogFragment {

    private TextView pid, toolbar_title, pname, serial;
    private EditText batch_no;
    private Toolbar toolbar;
    private EditText quantity, days, months, years, dd;
    ;
    private Button add_product;
    private TinyDB tinyDB;
    private PurchaseProduct purchaseProduct;
    private ImageView calendere;
    private ImageButton days_minus, days_plus, months_minus, months_plus, years_plus, years_minus;

    public PurchaseProductDetailsFragment() {
    }

    public PurchaseProductDetailsFragment(PurchaseProduct purchaseProduct) {

        this.purchaseProduct = purchaseProduct;

    }

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
        return inflater.inflate(R.layout.fragment_purchase_product_details, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initUI(view);

        setListners();

        // setExpiryDate();

        set_text();


        tinyDB = new TinyDB(getContext());

        quantity.setFilters(new InputFilter[]{new InputFilterMinMax("1", "1000000")});

    }


    private void initUI(View view) {

        pid = view.findViewById(R.id.pid);
        pname = view.findViewById(R.id.pname);
        batch_no = view.findViewById(R.id.batch_no);
        serial = view.findViewById(R.id.serial);
        quantity = view.findViewById(R.id.quantity);
        toolbar = view.findViewById(R.id.toolbar);
        add_product = view.findViewById(R.id.add_product);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        days_minus = view.findViewById(R.id.minus_days);
        days_plus = view.findViewById(R.id.plus_days);
        months_minus = view.findViewById(R.id.minus_months);
        months_plus = view.findViewById(R.id.plus_months);
        years_plus = view.findViewById(R.id.plus_years);
        years_minus = view.findViewById(R.id.minus_years);
        days = view.findViewById(R.id.days);
        years = view.findViewById(R.id.years);
        months = view.findViewById(R.id.months);
        dd = view.findViewById(R.id.dd);

    }


    private void setListners() {

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getTexts()) {
                    PurchaseProduct purchaseProduct1 = new PurchaseProduct();
                    purchaseProduct1.setSerialNo(serial.getText().toString());
                    purchaseProduct1.setProductName(pname.getText().toString());
                    if (Boolean.parseBoolean(purchaseProduct.getValidGTIN())) purchaseProduct1.setExpiry(dd.getText().toString());
                    else purchaseProduct1.setExpiry(getExpiryDate());
                    purchaseProduct1.setProductId(pid.getText().toString());
                    purchaseProduct1.setBatchNo(batch_no.getText().toString());
                    ((PurchaseCheckActivity) getActivity()).addProduct(purchaseProduct1, String.valueOf(quantity.getText().toString()));
                }
            }
        });

        if (!Boolean.parseBoolean(purchaseProduct.getValidGTIN())) {

            final TextWatcher tw = new TextWatcher() {
                private String current = "";
                private String ddmmyyyy = "ddMMYYYY";
                private Calendar cal = Calendar.getInstance();

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().equals(current)) {
                        String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                        String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                        int cl = clean.length();
                        int sel = cl;
                        for (int i = 2; i <= cl && i < 6; i += 2) {
                            sel++;
                        }
                        //Fix for pressing delete next to a forward slash
                        if (clean.equals(cleanC)) sel--;

                        if (clean.length() < 8) {
                            clean = clean + ddmmyyyy.substring(clean.length());
                        } else {
                            //This part makes sure that when we finish entering numbers
                            //the date is correct, fixing it otherwise
                            int day = Integer.parseInt(clean.substring(0, 2));
                            int mon = Integer.parseInt(clean.substring(2, 4));
                            int year = Integer.parseInt(clean.substring(4, 8));

                            mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                            cal.set(Calendar.MONTH, mon - 1);
                            year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                            cal.set(Calendar.YEAR, year);
                            // ^ first set year for the line below to work correctly
                            //with leap years - otherwise, date e.g. 29/02/2012
                            //would be automatically corrected to 28/02/2012

                            day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                            clean = String.format("%02d%02d%02d", day, mon, year);
                        }

                        clean = String.format("%s/%s/%s", clean.substring(0, 2),
                                clean.substring(2, 4),
                                clean.substring(4, 8));

                        sel = sel < 0 ? 0 : sel;
                        current = clean;
                        dd.setText(current);
                        dd.setSelection(sel < current.length() ? sel : current.length());
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }

            };
            dd.addTextChangedListener(tw);
        }
        else {
            dd.setInputType(InputType.TYPE_NULL);
        }
    }


    private void setExpiryDate() {

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        days.setText(String.valueOf(day));
        months.setText(String.valueOf(month));
        years.setText(String.valueOf(year));

        days.setFilters(new InputFilter[]{new InputFilterMinMax("1", "31")});
        months.setFilters(new InputFilter[]{new InputFilterMinMax("1", "12")});
        years.setFilters(new InputFilter[]{new InputFilterMinMax("1900", "2100")});

        days.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!b) {
                    if (days.getText().length() == 1)
                        days.setText(days.getText().insert(0, "0"));
                }
            }
        });

        months.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!b) {
                    if (months.getText().length() == 1)
                        months.setText(months.getText().insert(0, "0"));
                }
            }
        });


        days_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                days.setText(String.valueOf(Integer.parseInt(days.getText().toString()) + 1));
            }
        });

        days_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                days.setText(String.valueOf(Integer.parseInt(days.getText().toString()) - 1));
            }
        });

        months_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                months.setText(String.valueOf(Integer.parseInt(months.getText().toString()) - 1));
            }
        });

        months_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                months.setText(String.valueOf(Integer.parseInt(months.getText().toString()) + 1));
            }
        });

        years_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                years.setText(String.valueOf(Integer.parseInt(years.getText().toString()) - 1));
            }
        });

        years_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                years.setText(String.valueOf(Integer.parseInt(years.getText().toString()) + 1));
            }
        });
    }


    private String getExpiryDate() {

     /*   if (days.getText().length()==1)
            days.setText(days.getText().insert(0,"0"));

        if (months.getText().length()==1)
            months.setText(months.getText().insert(0,"0"));

        if (years.getText().length() < 3) years.setError(getResources().getString(R.string.enter_valid_year));

        days.getText().toString() + "/" + months.getText().toString() + "/" + years.getText().toString() ;
        */

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = format1.parse(dd.getText().toString().replace("/" , "-"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format2.format(date) ;

    }

    private void set_text() {

        toolbar_title.setText(purchaseProduct.getProductName());
        pname.setText(purchaseProduct.getProductName());
        pid.setText(purchaseProduct.getProductId());

        if (Boolean.parseBoolean(purchaseProduct.getValidGTIN())) {
         /*   days.setText(purchaseProduct.getExpiry().substring(0 , 2));
            months.setText(purchaseProduct.getExpiry().substring(3 , 5));
            days.setText(purchaseProduct.getExpiry().substring(6));

          */

         /*   SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            dd.setText(format.format(Date.parse(purchaseProduct.getExpiry())));
*/
            dd.setText(purchaseProduct.getExpiry());
            batch_no.setText(purchaseProduct.getBatchNo());
            serial.setText(purchaseProduct.getSerialNo());

            quantity.requestFocus();

            ((PurchaseCheckActivity) getActivity()).showKeyboard();

        } else {

            //  expired_date.setEnabled(true);
            batch_no.setEnabled(true);

            batch_no.requestFocus();

            ((PurchaseCheckActivity) getActivity()).showKeyboard();
        }
    }


    private boolean getTexts() {

    /*    if (TextUtils.isEmpty(days.getText())){

            days.setError(getResources().getString(R.string.enter_days));
            return false;
        }

        if (TextUtils.isEmpty(months.getText())){

            months.setError(getResources().getString(R.string.enter_months));
            return false;
        }

        if (TextUtils.isEmpty(years.getText())){

            years.setError(getResources().getString(R.string.enter_years));
            return false;
        }

     */
        if (TextUtils.isEmpty(batch_no.getText())) {

            batch_no.setError(getResources().getString(R.string.enter_batchno));
            return false;
        }
        if (dd.getText().toString().contains("d") || dd.getText().toString().contains("M") || dd.getText().toString().contains("Y")) {
            dd.setError(getResources().getString(R.string.enter_valid_date));
            return false;
        }

        if (TextUtils.isEmpty(quantity.getText())) {
            quantity.setError(getResources().getString(R.string.enter_quantity));
            return false;
        } else return true;
    }
}
