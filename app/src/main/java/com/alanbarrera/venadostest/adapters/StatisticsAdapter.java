package com.alanbarrera.venadostest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alanbarrera.venadostest.R;
import com.alanbarrera.venadostest.models.Statistic;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    // List of statistics.
    private final ArrayList<Statistic> mStatistics;

    public StatisticsAdapter(ArrayList<Statistic> statistics) {
        mStatistics = statistics;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        // Set data to the view holder.

        Statistic statistic = mStatistics.get(position);

        holder.mPosition.setText(String.valueOf(statistic.getPosition()));
        holder.mName.setText(statistic.getTeam());
        holder.mGames.setText(String.valueOf(statistic.getGames()));
        holder.mScoreDiff.setText(String.valueOf(statistic.getScoreDiff()));
        holder.mPoints.setText(String.valueOf(statistic.getPoints()));
        Glide.with(holder.itemView).load(statistic.getImage()).into(holder.mShield);

    }

    @Override
    public int getItemCount() {
        return mStatistics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // Declare fields.
        public final View mView;
        public final TextView mPosition;
        public final ImageView mShield;
        public final TextView mName;
        public final TextView mGames;
        public final TextView mScoreDiff;
        public final TextView mPoints;


        public ViewHolder(View view)
        {
            // Initialize fields.
            super(view);
            mView = view;
            mPosition = view.findViewById(R.id.stats_position);
            mShield = view.findViewById(R.id.stats_shield);
            mName = view.findViewById(R.id.stats_name);
            mGames = view.findViewById(R.id.stats_games);
            mScoreDiff = view.findViewById(R.id.stats_diff);
            mPoints = view.findViewById(R.id.stats_points);
        }
    }

    /**
     * Update the data of the recyler view.
     * @param updatedStatistics The updated list of statistics.
     */
    public void updateStatistics(ArrayList<Statistic> updatedStatistics)
    {
        mStatistics.clear();
        mStatistics.addAll(updatedStatistics);
        this.notifyDataSetChanged();
    }
}
