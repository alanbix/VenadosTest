package com.alanbarrera.venadostest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alanbarrera.venadostest.R;
import com.alanbarrera.venadostest.fragments.PlayersFragment.OnListFragmentPlayerInteractionListener;
import com.alanbarrera.venadostest.models.Player;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder>
{

    // List of players.
    private final ArrayList<Player> mPlayers;

    // Interaction listener.
    private final OnListFragmentPlayerInteractionListener mListener;

    /**
     * Create new PlayersAdapter
     * @param players List of players
     * @param listener Listener for click event
     */
    public PlayersAdapter(ArrayList<Player> players, OnListFragmentPlayerInteractionListener listener) {
        mPlayers = players;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        // Set data to the view holder.

        Player player = mPlayers.get(position);
        holder.mPlayer = player;
        holder.mPlayerPosition.setText(player.getPosition());
        String name = player.getName().split(" ")[0] + " " + player.getFirstSurname();
        holder.mPlayerName.setText(name);
        Glide.with(holder.itemView).load(player.getImage()).into(holder.mPlayerImage);

        // Set the OnClickListener of the view holder.
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentPlayerInteraction(holder.mPlayer);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // Declare fields.
        public final View mView;
        public final CircleImageView mPlayerImage;
        public final TextView mPlayerPosition;
        public final TextView mPlayerName;
        public Player mPlayer;


        public ViewHolder(View view)
        {
            // Initialize fields.
            super(view);
            mView = view;
            mPlayerImage = view.findViewById(R.id.player_image);
            mPlayerPosition = view.findViewById(R.id.player_position);
            mPlayerName = view.findViewById(R.id.player_name);
        }
    }

    /**
     * Update the data of the recyler view.
     * @param updatedPlayers The updated list of players.
     */
    public void updatePlayers(ArrayList<Player> updatedPlayers)
    {
        mPlayers.clear();
        mPlayers.addAll(updatedPlayers);
        this.notifyDataSetChanged();
    }
}
