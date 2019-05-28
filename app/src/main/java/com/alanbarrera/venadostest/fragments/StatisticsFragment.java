package com.alanbarrera.venadostest.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alanbarrera.venadostest.R;
import com.alanbarrera.venadostest.adapters.StatisticsAdapter;
import com.alanbarrera.venadostest.models.Statistic;
import com.alanbarrera.venadostest.utils.Constants;

import java.util.ArrayList;

/**
 * A fragment representing a list of Statistics.
 */
public class StatisticsFragment extends Fragment
{
    private ArrayList<Statistic> mStatistics = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StatisticsFragment() {
    }


    public static StatisticsFragment newInstance(ArrayList<Statistic> statistics) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.STATISTICS_KEY, statistics);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStatistics = (ArrayList<Statistic>)getArguments().getSerializable(Constants.STATISTICS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new StatisticsAdapter(mStatistics));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Update the data of the recyler view.
     */
    public void updateStatistics()
    {
        if (getArguments() != null) {
            mStatistics = (ArrayList<Statistic>)getArguments().getSerializable(Constants.STATISTICS_KEY);
            RecyclerView recyclerView = (RecyclerView) getView();
            StatisticsAdapter statisticsAdapter = (StatisticsAdapter) recyclerView.getAdapter();
            statisticsAdapter.updateStatistics(mStatistics);
        }
    }
}
