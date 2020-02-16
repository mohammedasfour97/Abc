package com.truevisionsa.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.travijuu.numberpicker.library.NumberPicker;
import com.truevisionsa.ModelItems.SaleItem;
import com.truevisionsa.ModelItems.TransferItem;
import com.truevisionsa.R;
import com.truevisionsa.SalesOrderAndRelocateCheck.Views.CheckSalesItemsActivity;
import com.truevisionsa.SalesOrderAndRelocateCheck.Views.CheckTransferItemsActivity;
import com.truevisionsa.SalesOrderAndRelocateCheck.Views.TransferListActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesItemsListFragment extends DialogFragment {

    private List<SaleItem> _saleItemList;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private EditText numberPicker;
    private Button save, cancel;
    private List<SaleItem> itemsList;
    private List<TransferItem> transferItemList;
    private CheckItemsActivityAdapter mAdapter;
    private CheckItemsActivityAdapter2 mAdapter2;
    private SaleItem _saleItem;
    private TransferItem _transferItem;
    private TextView pack_qnt;
    private LinearLayout units_in_pack_layout;
    private NumberPicker units_in_pack_no;

    public SalesItemsListFragment() {
    }

    public SalesItemsListFragment (List<SaleItem> saleItemList , boolean count) {

        _saleItemList = saleItemList;

        Bundle args = new Bundle();

        args.putBoolean("count" , count);

        setArguments(args);

    }

    public SalesItemsListFragment(List<TransferItem> saleItemList , boolean count , String g) {

         transferItemList= saleItemList;

        Bundle args = new Bundle();

        args.putBoolean("count" , count);

        setArguments(args);

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
        return inflater.inflate(R.layout.fragment_sales_items_list, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        setListners();
        initRecyclerView();

        if (getArguments().getBoolean("count")){

            numberPicker.setEnabled(true);
            pack_qnt.setEnabled(true);
        }


    }

    private void initUI(View view){

        progressBar = view.findViewById(R.id.progress);
        recyclerView = view.findViewById(R.id.recyclerview);
        numberPicker = view.findViewById(R.id.packs_qnt);
        save = view.findViewById(R.id.save);
        cancel = view.findViewById(R.id.cancel);
         pack_qnt= view.findViewById(R.id.pack_qnt_titile);
        units_in_pack_layout= view.findViewById(R.id.units_in_pack_layout);
        units_in_pack_no = view.findViewById(R.id.units_in_pack_no);
    }

    private void setListners(){

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (transferItemList != null){

                        if (_transferItem == null)
                        Toast.makeText(getContext(), getResources().getString(R.string.choose_product), Toast.LENGTH_SHORT).show();

                        else {
                            if (getArguments().getBoolean("count"))
                                ((CheckTransferItemsActivity) getActivity()).fillCurrentList(_transferItem, Integer.parseInt(numberPicker.getText().toString()),
                                        units_in_pack_no.getValue());

                            else
                                ((CheckTransferItemsActivity) getActivity()).fillCurrentList(_transferItem, 1, units_in_pack_no.getValue());

                            dismiss();

                        }

                    }

                    else {

                        if (_saleItem == null)
                            Toast.makeText(getContext(), getResources().getString(R.string.choose_product), Toast.LENGTH_SHORT).show();

                        else {

                            if (getArguments().getBoolean("count"))
                                ((CheckSalesItemsActivity) getActivity()).fillCurrentList(_saleItem, Integer.parseInt(numberPicker.getText().toString()));

                            else
                                ((CheckSalesItemsActivity) getActivity()).fillCurrentList(_saleItem, 1);

                            dismiss();

                        }

                    }

            }
        });
    }


    private void initRecyclerView(){



        if (transferItemList != null){

            mAdapter2 = new SalesItemsListFragment.CheckItemsActivityAdapter2(getContext(), transferItemList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter2);
            recyclerView.setNestedScrollingEnabled(false);

            mAdapter2.notifyDataSetChanged();

            _transferItem = null;
        }

        else {
            itemsList = new ArrayList<>();


            mAdapter = new SalesItemsListFragment.CheckItemsActivityAdapter(getContext(), itemsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setNestedScrollingEnabled(false);

            itemsList.clear();
            itemsList.addAll(_saleItemList);
            mAdapter.notifyDataSetChanged();

            _saleItem = null;
        }

    }


    public class CheckItemsActivityAdapter2 extends RecyclerView.Adapter<SalesItemsListFragment.CheckItemsActivityAdapter2.MyViewHolder> {
        private Context context;
        private List<TransferItem> transferItemList;



        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView pname , product_id , stock_id , expiry_date ;
            private ImageView lock;
            private LinearLayout contex;


            public MyViewHolder(View view) {
                super(view);
                pname = view.findViewById(R.id.pname);
                product_id = view.findViewById(R.id.product_id);
                stock_id = view.findViewById(R.id.stoke_id);
                expiry_date = view.findViewById(R.id.expired_date);
                lock = view.findViewById(R.id.lock);
                contex = view.findViewById(R.id.contex);
                context = itemView.getContext();


            }
        }


        public CheckItemsActivityAdapter2(Context context, List<TransferItem> saleItemList) {
            this.context = context;
            this.transferItemList = saleItemList;
        }

        @Override
        public CheckItemsActivityAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sale_item_item, parent, false);

            return new SalesItemsListFragment.CheckItemsActivityAdapter2.MyViewHolder(itemView);
        }

        public void onBindViewHolder(final CheckItemsActivityAdapter2.MyViewHolder holder, final int position) {

            final TransferItem saleItem = transferItemList.get(position);

            holder.pname.setText(saleItem.getProduct());

          /*  holder.product_id.setText(saleItem.getPack_qnt() + " " + getResources().getString(R.string.packs) + " " + saleItem.getUnits_in_pack() +
                    " " + getResources().getString(R.string.units));
*/
            holder.stock_id.setText(saleItem.getStock_id());


            //   holder.expiry_date.setText(curFormater.format(Date.parse(product.getExpiry().substring(0 , 10))));

            holder.expiry_date.setText(saleItem.getExpiry().substring(0 , 10));

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        /*    Date date1 = new java.util.Date();
            long diff = 0;

            Date date2 = null;
            try {
                date2 = df.parse(saleItem.getExpiry());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            diff = date1.getTime() - date2.getTime();

            if (diff > 0) holder.pname.setTextColor(Color.RED);
*/
            try {

                if (df.parse(saleItem.getExpiry()).before(new Date())) holder.pname.setTextColor(Color.RED);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    initRecyclerView();

                    _transferItem = saleItem;

                    units_in_pack_no.setMax(Integer.parseInt(saleItem.getUnits_in_pack()) -1);

                    if (transferItemList != null) units_in_pack_layout.setVisibility(View.VISIBLE);

                    notifyDataSetChanged();
                }
            });

            if (_transferItem == saleItem) holder.contex.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        }

        @Override
        public int getItemCount() {
            return transferItemList.size();
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }




    public class CheckItemsActivityAdapter extends RecyclerView.Adapter<SalesItemsListFragment.CheckItemsActivityAdapter.MyViewHolder> {
        private Context context;
        private List<SaleItem> saleItemslist;

    public CheckItemsActivityAdapter(Context context, List<SaleItem> saleItemList) {
        this.context = context;
        this.saleItemslist = saleItemList;
    }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView pname , product_id , stock_id , expiry_date ;
            private ImageView lock;
            private LinearLayout contex;


            public MyViewHolder(View view) {
                super(view);
                pname = view.findViewById(R.id.pname);
                product_id = view.findViewById(R.id.product_id);
                stock_id = view.findViewById(R.id.stoke_id);
                expiry_date = view.findViewById(R.id.expired_date);
                lock = view.findViewById(R.id.lock);
                contex = view.findViewById(R.id.contex);
                context = itemView.getContext();


            }
        }

    @Override
    public SalesItemsListFragment.CheckItemsActivityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sale_item_item, parent, false);

        return new SalesItemsListFragment.CheckItemsActivityAdapter.MyViewHolder(itemView);
    }

    public void onBindViewHolder(final CheckItemsActivityAdapter.MyViewHolder holder, final int position) {

        final SaleItem saleItem = saleItemslist.get(position);

        holder.pname.setText(saleItem.getProduct());

     //   holder.product_id.setText(saleItem.getPacks_qnt() + " " + getResources().getString(R.string.items));

        holder.stock_id.setText(saleItem.getStock_id());


        //   holder.expiry_date.setText(curFormater.format(Date.parse(product.getExpiry().substring(0 , 10))));

        holder.expiry_date.setText(saleItem.getExpiry().substring(0 , 10));

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        /*    Date date1 = new java.util.Date();
            long diff = 0;

            Date date2 = null;
            try {
                date2 = df.parse(saleItem.getExpiry());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            diff = date1.getTime() - date2.getTime();

            if (diff > 0) holder.pname.setTextColor(Color.RED);
*/
        try {

            if (df.parse(saleItem.getExpiry()).before(new Date())) holder.pname.setTextColor(Color.RED);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initRecyclerView();

                _saleItem = saleItem;

                notifyDataSetChanged();
            }
        });

        if (_saleItem == saleItem) holder.contex.setBackgroundColor(getResources().getColor(R.color.colorAccent));

    }

    @Override
    public int getItemCount() {
        return saleItemslist.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
}
