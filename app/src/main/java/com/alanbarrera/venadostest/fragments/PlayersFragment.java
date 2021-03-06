package com.alanbarrera.venadostest.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alanbarrera.venadostest.R;
import com.alanbarrera.venadostest.adapters.PlayersAdapter;
import com.alanbarrera.venadostest.models.Player;
import com.alanbarrera.venadostest.utils.Constants;

import java.util.ArrayList;

/**
 * A fragment representing a list of Players.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentPlayerInteractionListener}
 * interface.
 */
public class PlayersFragment extends Fragment
{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = Constants.PLAYERS_GRID_COLUMNS;
    private OnListFragmentPlayerInteractionListener mListener;
    private ArrayList<Player> mPlayers;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PlayersFragment() {
    }

    public static PlayersFragment newInstance(ArrayList<Player> players, int columnCount) {
        PlayersFragment fragment = new PlayersFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.PLAYERS_KEY, players);
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mPlayers = (ArrayList<Player>) getArguments().getSerializable(Constants.PLAYERS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_players, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new PlayersAdapter(mPlayers, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentPlayerInteractionListener) {
            mListener = (OnListFragmentPlayerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentPlayerInteractionListener {
        void onListFragmentPlayerInteraction(Player player);
    }

    /**
     * Update the data of the recyler view.
     */
    public void updatePlayers()
    {
        if (getArguments() != null) {
            mPlayers = (ArrayList<Player>)getArguments().getSerializable("PLAYERS");
            RecyclerView recyclerView = (RecyclerView) getView();
            PlayersAdapter playersAdapter = (PlayersAdapter) recyclerView.getAdapter();
            playersAdapter.updatePlayers(mPlayers);
        }
    }
}
