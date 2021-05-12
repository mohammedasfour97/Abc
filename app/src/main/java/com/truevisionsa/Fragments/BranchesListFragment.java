package com.truevisionsa.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.truevisionsa.Branches.BranchesPresenter;
import com.truevisionsa.Branches.Contract;
import com.truevisionsa.Utils.DatabaseHelper;
import com.truevisionsa.ModelItems.Branch;
import com.truevisionsa.ModelItems.Config;
import com.truevisionsa.R;
import com.truevisionsa.Utils.TinyDB;
import com.truevisionsa.UserPriviliges.UserPriviligesActivity;

import java.util.ArrayList;
import java.util.List;

public class BranchesListFragment extends DialogFragment implements Contract.View {

    private RecyclerView recyclerView;
    private List<Branch> itemsList;
    private BranchesListFragment.BranchesListFragmentAdapter mAdapter;
    private BranchesPresenter presenter ;
    private Config config ;
    private DatabaseHelper databaseHelper;
    private TinyDB tinyDB ;
    private String branch_id , company_id , branch_title , glnno , dtt_slic;

    public BranchesListFragment() {
    }

    public static BranchesListFragment newInstance() {

        BranchesListFragment branchesListFragment = new BranchesListFragment();


        return branchesListFragment;
    }

    @Override
    public void onStart()
    {
        super.onStart();

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
        return inflater.inflate(R.layout.fragment_branches, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setCancelable(false);

        initRecyclerView(view);

        databaseHelper = new DatabaseHelper(getContext());
        tinyDB = new TinyDB(getContext());
        config = databaseHelper.getUser().get(0);

        presenter = new BranchesPresenter(this, getContext());

        presenter.requestBranches(config);
    }


    private void initRecyclerView(View view){

        recyclerView = view.findViewById(R.id.recyclerview);

        itemsList = new ArrayList<>();
        mAdapter = new BranchesListFragment.BranchesListFragmentAdapter(getContext(), itemsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }


    @Override
    public void fillRecyclerView(List<Branch> result) {

        itemsList.clear();
        itemsList.addAll(result);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinished() {

        tinyDB.putString("branch_id" , branch_id);
        tinyDB.putString("company_id" , company_id);
        tinyDB.putString("glnno" , glnno);
        tinyDB.putString("dtts" , dtt_slic);

        ((UserPriviligesActivity)getActivity()).branch_tile.setText(branch_title);

        dismiss();
    }

    @Override
    public void onFailure(int error) {

        dismiss();
    }


    public class BranchesListFragmentAdapter extends RecyclerView.Adapter<BranchesListFragment.BranchesListFragmentAdapter.MyViewHolder> {
        private Context context;
        private List<Branch> branchList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            Context context;
            private TextView name ;


            public MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.name);
                context = itemView.getContext();


            }
        }


        public BranchesListFragmentAdapter(Context context, List<Branch> branchList) {
            this.context = context;
            this.branchList = branchList;
        }

        @Override
        public BranchesListFragment.BranchesListFragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.branch_item, parent, false);

            return new BranchesListFragment.BranchesListFragmentAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final BranchesListFragment.BranchesListFragmentAdapter.MyViewHolder holder, final int position) {

            final Branch branch = branchList.get(position);

            holder.name.setText(branch.getBranch_name());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    branch_id = branch.getBranch_id();
                    company_id = branch.getCompany_id();
                    branch_title = branch.getBranch_name();
                    glnno = branch.getGlnno();
                    dtt_slic = branch.getBranch_dttslic();

                    presenter.requestEditDevice(branch.getBranch_id() , tinyDB.getString("user_id") , tinyDB.getString("id") ,
                            databaseHelper.getUser().get(0));


                }
            });
        }

        @Override
        public int getItemCount() {
            return branchList.size();
        }


    }
}
