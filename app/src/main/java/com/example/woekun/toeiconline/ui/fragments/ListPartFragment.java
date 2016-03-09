package com.example.woekun.toeiconline.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.customs.RecyclerItemClickListener;
import com.example.woekun.toeiconline.adapters.PartListAdapter;

public class ListPartFragment extends Fragment {

    public  ListPartFragment() {
        // Required empty public constructor
    }

    public static ListPartFragment newInstance() {
        ListPartFragment fragment = new ListPartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_part, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view){
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.part_list);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new PartListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.main, QuestionFragment.newInstance(position + 1))
                                .addToBackStack(null)
                                .commit();
                    }
                })
        );
    }
}
