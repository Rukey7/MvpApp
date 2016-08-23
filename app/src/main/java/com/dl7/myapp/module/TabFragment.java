package com.dl7.myapp.module;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dl7.helperlibrary.helper.RecyclerViewHelper;
import com.dl7.myapp.R;
import com.dl7.myapp.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2016/4/15.
 */
public class TabFragment extends Fragment {

    public static final String FRAG_KEY = "FragKey";

    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    private ListAdapter mAdapter;


    public static TabFragment newInstance(String title) {
        TabFragment fragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAG_KEY, title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, null);
        ButterKnife.bind(this, view);
        mAdapter = new ListAdapter(getContext());
        RecyclerViewHelper.initRecyclerViewV(getContext(), mRvList, true, mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            String title = getArguments().getString(FRAG_KEY);
            List<String> titles = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                titles.add(title + " " + i);
            }
            mAdapter.updateItems(titles);
        }
    }
}
