package com.alanbarrera.venadostest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alanbarrera.venadostest.R;
import com.alanbarrera.venadostest.fragments.GamesFragment.OnListFragmentInteractionListener;
import com.alanbarrera.venadostest.models.Game;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder>
{

    private final ArrayList<Game> mGames;
    private final OnListFragmentInteractionListener mListener;

    public GameAdapter(ArrayList<Game> items, OnListFragmentInteractionListener listener)
    {
        mGames = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        Game game = mGames.get(position);

        holder.mItem = game;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(game.getDatetime());

        SimpleDateFormat dateFormat = new SimpleDateFormat("EE",Locale.forLanguageTag("es-MX"));

        String dayNumber = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        holder.mDayNumber.setText(dayNumber);
        holder.mDayName.setText(dateFormat.format(game.getDatetime()).toUpperCase());

        String score = game.getHomeScore() + " - " + game.getAwayScore();
        holder.score.setText(score);

        if(game.isLocal()) {
            holder.localName.setText(R.string.venados_name);
            holder.opponentName.setText(game.getOpponent());
            Glide.with(holder.itemView).load(R.drawable.logo_venados_fc).into(holder.localImage);
            Glide.with(holder.itemView).load(game.getOpponentImage()).into(holder.opponentImage);
        }
        else {
            holder.localName.setText(game.getOpponent());
            holder.opponentName.setText(R.string.venados_name);
            Glide.with(holder.itemView).load(game.getOpponentImage()).into(holder.localImage);
            Glide.with(holder.itemView).load(R.drawable.logo_venados_fc).into(holder.opponentImage);
        }

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        // Calendar date
        public final TextView mDayNumber;
        public final TextView mDayName;

        // Local info
        public final ImageView localImage;
        public final TextView localName;

        // Score
        public final TextView score;

        // Opponent info
        public final ImageView opponentImage;
        public final TextView opponentName;

        public Game mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;

            mDayNumber = view.findViewById(R.id.day_number);
            mDayName = view.findViewById(R.id.day_name);
            localImage = view.findViewById(R.id.local_image);
            localName = view.findViewById(R.id.local_name);
            score = view.findViewById(R.id.score);
            opponentImage = view.findViewById(R.id.opponent_image);
            opponentName = view.findViewById(R.id.opponent_name);
        }
    }
}
