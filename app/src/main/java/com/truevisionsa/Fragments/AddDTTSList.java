package com.truevisionsa.Fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.truevisionsa.Auth.LoginActivity;
import com.truevisionsa.DTTSScan.Contract;
import com.truevisionsa.DTTSScan.DTTSScanActivity;
import com.truevisionsa.DTTSScan.DTTSScanPresenter;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.ModelItems.DTTSList;
import com.truevisionsa.ModelItems.IDName;
import com.truevisionsa.R;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.Utils.TinyDB;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

public class AddDTTSList extends DialogFragment implements Contract.DTTSScan.View.FragmentView {

    private EditText list_ref;
    private Spinner list_types;
    private Button add_list, cancel;
    private ArrayAdapter<IDName> list_types_adapter;
    private IDName selected_type;
    private List<IDName> types_list;
    private TinyDB tinyDB;
    private Config config;
    private DTTSScanPresenter dTTSTransferPresenter;


    public AddDTTSList() {
    }

    public static AddDTTSList newInstance() {

        AddDTTSList addDTTSList = new AddDTTSList();

        return addDTTSList;
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
        return inflater.inflate(R.layout.fragment_add_dtts_list, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.setCancelable(false);

        initUI(view);

        setListners();

        initSpinners();

        tinyDB = new TinyDB(getContext());

        config = new DatabaseHelper(getContext()).getUser().get(0);

        dTTSTransferPresenter = new DTTSScanPresenter(this , getContext());

        dTTSTransferPresenter.requestListTypes(config);
    }


    private void initUI(View view) {

        list_types = view.findViewById(R.id.types);
        list_ref = view.findViewById(R.id.list_ref);
        add_list = view.findViewById(R.id.add_list);
        cancel = view.findViewById(R.id.cancel);
    }


    private void setListners() {

        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (get_validation()){

                    showProgress();

                    dTTSTransferPresenter.requestAddList(tinyDB.getString("branch_id"), tinyDB.getString("id"),
                            tinyDB.getString("user_id"), list_ref.getText().toString(), selected_type.getId(), config);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((DTTSScanActivity)getActivity()).requestDTTSTransferList();

                dismiss();
            }
        });
    }

    private void initSpinners(){

        types_list = new ArrayList<>();

        list_types_adapter = new ArrayAdapter<IDName>(getContext(), android.R.layout.simple_spinner_item, types_list);
        list_types_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
        list_types.setAdapter(list_types_adapter);

        resetSpinners();
        setSpinnersListener();
    }


    private void resetSpinners(){

        types_list.clear();
        types_list.add(new IDName("-1","",getResources().getString(R.string.choose_list_type)));
        list_types_adapter.notifyDataSetChanged();
    }

    private void setSpinnersListener() {

        list_types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selected_type = ((IDName) parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean get_validation(){

        boolean b = true;

        if (list_ref.getText().toString().equals("")){

            b = false;
            list_ref.setError(getResources().getString(R.string.enter_dtts_list_ref));
        }

        else if (selected_type.getId().equals("-1")){

            b = false;
            Toast.makeText(getContext(), getResources().getString(R.string.choose_dtts_list_type), Toast.LENGTH_SHORT).show();
        }

        return b;
    }

    @Override
    public void fillListTypesList(List<IDName> listTypesList) {

        resetSpinners();

        types_list.addAll(listTypesList);

        list_types_adapter.notifyDataSetChanged();
    }

    @Override
    public void onListAdded() {

        Toast.makeText(getContext(), getResources().getString(R.string.succ_add_dtts_list), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailure(String error) {

        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailure(int error) {

        Toast.makeText(getContext(), getResources().getString(error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

        ((DTTSScanActivity)getActivity()).showProgress();
    }

    @Override
    public void hideProgress() {

        ((DTTSScanActivity)getActivity()).hideProgress();
    }

}
