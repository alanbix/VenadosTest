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

public class GameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    // List of games.
    private final ArrayList<Game> mGames;

    // Interaction listener.
    private final OnListFragmentInteractionListener mListener;

    /**
     * Create new GameAdapater
     * @param games List of games
     * @param listener Listener for click event
     */
    public GameAdapter(ArrayList<Game> games, OnListFragmentInteractionListener listener)
    {
        mGames = games;
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position)
    {
        // If opponent field IS NOT empty, then it IS NOT a header, return 0,
        // otherwise return one.
        return  mGames.get(position).getOpponent() != null ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view;

        if(viewType == 0)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
            return new GameViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_header, parent, false);
            return new GameHeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {
        Game game = mGames.get(position);

        if (holder.getItemViewType() == 0) {
            final GameViewHolder gameViewHolder = (GameViewHolder) holder;

            // Set data to the view holder.
            gameViewHolder.mItem = game;

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(game.getDatetime());

            SimpleDateFormat dateFormat = new SimpleDateFormat("EE", Locale.forLanguageTag("es-MX"));

            String dayNumber = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            gameViewHolder.mDayNumber.setText(dayNumber);
            gameViewHolder.mDayName.setText(dateFormat.format(game.getDatetime()).toUpperCase());

            String score = game.getHomeScore() + " - " + game.getAwayScore();
            gameViewHolder.score.setText(score);

            if (game.isLocal()) {
                gameViewHolder.localName.setText(R.string.venados_name);
                gameViewHolder.opponentName.setText(game.getOpponent());
                Glide.with(gameViewHolder.itemView).load(R.drawable.logo_venados_fc).into(gameViewHolder.localImage);
                Glide.with(gameViewHolder.itemView).load(game.getOpponentImage()).into(gameViewHolder.opponentImage);
            } else {
                gameViewHolder.localName.setText(game.getOpponent());
                gameViewHolder.opponentName.setText(R.string.venados_name);
                Glide.with(gameViewHolder.itemView).load(game.getOpponentImage()).into(gameViewHolder.localImage);
                Glide.with(gameViewHolder.itemView).load(R.drawable.logo_venados_fc).into(gameViewHolder.opponentImage);
            }

            // Set the OnClickListener of the view holder.
            gameViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(gameViewHolder.mItem);
                    }
                }
            });
        }
        else
        {
            final GameHeaderViewHolder gameHeaderViewHolder = (GameHeaderViewHolder) holder;
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.forLanguageTag("es-MX"));
            gameHeaderViewHolder.mHeader.setText(monthFormat.format(game.getDatetime()).toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder
    {
        // Declare fields.

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

        public GameViewHolder(View view)
        {
            // Initialize fields

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

    public class GameHeaderViewHolder extends RecyclerView.ViewHolder
    {
        // Declare fields.
        public final View mView;
        public final TextView mHeader;

        public GameHeaderViewHolder(View view)
        {
            // Initialize fields

            super(view);
            mView = view;
            mHeader = view.findViewById(R.id.game_header_text);
        }
    }

    /**
     * Update the data of the recyler view.
     * @param updatedGames The updated list of games.
     */
    public void updateGames(ArrayList<Game> updatedGames)
    {
        mGames.clear();
        mGames.addAll(updatedGames);
        this.notifyDataSetChanged();
    }
}
