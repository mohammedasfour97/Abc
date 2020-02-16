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
import android.widget.Toast;

import com.travijuu.numberpicker.library.NumberPicker;
import com.truevisionsa.Products.Views.InvProductsActivity;
import com.truevisionsa.R;

public class EditInvFragment extends DialogFragment {

    private Toolbar toolbar;
    private NumberPicker no_in_pick ;
    private EditText pick_no ;
    private Button save , cancel ;

    public EditInvFragment() {
    }

    public static EditInvFragment newInstance(String max_units , String name , String packs_qn , String units_qo) {

        EditInvFragment editInvFragment = new EditInvFragment();

        Bundle args = new Bundle();

        args.putString("max" , max_units);
        args.putString("pname" , name);
        args.putString("packs_qn" , packs_qn);
        args.putString("units_qn" , units_qo);

        editInvFragment.setArguments(args);

        return editInvFragment;
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
        return inflater.inflate(R.layout.fragment_edit_inv, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initUI(view);

        setListners();

        toolbar.setTitle(getArguments().getString("pname"));

        pick_no.setText(getArguments().getString("packs_qn"));

        pick_no.requestFocus();
        pick_no.setFocusable(true);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        no_in_pick.setValue(Integer.parseInt(getArguments().getString("units_qn")));
        no_in_pick.setMax(Integer.parseInt(getArguments().getString("max")) - 1);


    }


    private void initUI(View view){

        toolbar = view.findViewById(R.id.toolbar);
        no_in_pick = view.findViewById(R.id.units_in_pack_no);
        pick_no = view.findViewById(R.id.packs_qn);
        save = view.findViewById(R.id.save);
        cancel = view.findViewById(R.id.cancel);

    }


    private void setListners(){

        save.setOnClickListener(new View.OnClickListener() {
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

                ((InvProductsActivity)getActivity()).editInvProduct(pick_no.getText().toString() , String.valueOf(no_in_pick.getValue()));

                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}

