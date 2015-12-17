package com.cui.animas.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cui.animas.R;
import com.cui.animas.adapter.Simple_IMG_Adapter;

import java.util.ArrayList;
import java.util.List;


public class CircularReveal_fragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";

    public static CircularReveal_fragment createInstance(int itemsCount) {
        CircularReveal_fragment partThreeFragment = new CircularReveal_fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        partThreeFragment.setArguments(bundle);
        return partThreeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(container.getContext());
        setupRecyclerView(recyclerView);
        return recyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        Simple_IMG_Adapter adapter = new Simple_IMG_Adapter(getArguments().getInt(ITEMS_COUNT_KEY),getActivity());
        recyclerView.setAdapter(adapter);
    }
}
