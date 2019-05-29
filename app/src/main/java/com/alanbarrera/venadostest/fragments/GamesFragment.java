package com.alanbarrera.venadostest.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alanbarrera.venadostest.R;
import com.alanbarrera.venadostest.adapters.GameAdapter;
import com.alanbarrera.venadostest.models.Game;
import com.alanbarrera.venadostest.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A fragment representing a list of Games.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GamesFragment extends Fragment
{
    private ArrayList<Game> mGames;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GamesFragment() {
    }

    public static GamesFragment newInstance(ArrayList<Game> games) {
        GamesFragment fragment = new GamesFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.GAMES_KEY, games);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mGames = (ArrayList<Game>)getArguments().getSerializable(Constants.GAMES_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            addHeadersTo(mGames);
            recyclerView.setAdapter(new GameAdapter(mGames, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Game item);
    }

    /**
     * Update the data of the recyler view.
     */
    public void updateGames()
    {
        if (getArguments() != null) {
            mGames = (ArrayList<Game>)getArguments().getSerializable(Constants.GAMES_KEY);
            RecyclerView recyclerView = (RecyclerView) getView();
            GameAdapter gameAdapter = (GameAdapter) recyclerView.getAdapter();
            addHeadersTo(mGames);
            gameAdapter.updateGames(mGames);
        }
    }

    private void addHeadersTo(ArrayList<Game> games)
    {
        if(games.size() > 0)
        {
            int lastMonthHeader = 0;

            Collections.sort(games, new Comparator<Game>()
            {
                public int compare(Game game1, Game game2)
                {
                    if (game1.getDatetime() == null || game2.getDatetime() == null)
                        return 0;

                    return game1.getDatetime().compareTo(game2.getDatetime());
                }
            });

            for (int i = 0; i < games.size(); i++)
            {
                Game game = games.get(i);

                if (game.getDatetime().getMonth() > lastMonthHeader)
                {
                    games.add(i, new Game(game.getDatetime()));
                    lastMonthHeader = game.getDatetime().getMonth();
                    i++;
                }
            }
        }
    }
}
