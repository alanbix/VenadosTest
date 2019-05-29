package com.alanbarrera.venadostest.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.alanbarrera.venadostest.R;
import com.alanbarrera.venadostest.fragments.GamesFragment;
import com.alanbarrera.venadostest.fragments.PlayersFragment;
import com.alanbarrera.venadostest.fragments.StatisticsFragment;
import com.alanbarrera.venadostest.interfaces.IVenadosApiService;
import com.alanbarrera.venadostest.models.Game;
import com.alanbarrera.venadostest.models.Player;
import com.alanbarrera.venadostest.models.Statistic;
import com.alanbarrera.venadostest.network.VenadosApiService;
import com.alanbarrera.venadostest.utils.Constants;
import com.alanbarrera.venadostest.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GamesFragment.OnListFragmentInteractionListener,
        PlayersFragment.OnListFragmentPlayerInteractionListener
{
    // Indicates the fragment that is active.
    enum ActiveFragment{GAMES, STATISTICS, PLAYERS}

    // Venados API Service
    IVenadosApiService apiService;

    // Swipe Refresh Layout
    private SwipeRefreshLayout swipeRefreshLayout;

    // ArrayLists for fragments.
    private ArrayList<Game> games;
    private ArrayList<Game> copaMx;
    private ArrayList<Game> ascensoMx;
    private ArrayList<Statistic> statistics;
    private ArrayList<Player> players;

    // The active fragment.
    private ActiveFragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the Support Action Toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set the tabs layout
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText(R.string.tab_copa_mx));
        tabs.addTab(tabs.newTab().setText(R.string.tab_ascenso_mx));
        tabs.addOnTabSelectedListener(getTabListener());

        // Set the swipe refresh layout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(getRefreshListener());

        // Set the navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize the API service
        apiService = VenadosApiService.getService();

        // Initialize the lists
        copaMx = new ArrayList<>();
        ascensoMx = new ArrayList<>();
        games = new ArrayList<>();
        statistics = new ArrayList<>();
        players = new ArrayList<>();

        // The Games Fragment is shown by default.
        activeFragment = ActiveFragment.GAMES;
        showFragment(activeFragment);
    }

    @Override
    public void onBackPressed()
    {
        // If drawer is open then close it, else, exit the application.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Show the loading icon.
        swipeRefreshLayout.setRefreshing(true);

        // Show the corresponding fragment depending on the
        // item selected.
        switch (item.getItemId())
        {
            case R.id.nav_home:
                showTabs(true);
                showStatsHeader(false);
                showFragment(ActiveFragment.GAMES);
                loadGames();
                break;
            case R.id.nav_statistics:
                showTabs(false);
                showStatsHeader(true);
                showFragment(ActiveFragment.STATISTICS);
                loadStatistics();
                break;
            case R.id.nav_players:
                showTabs(false);
                showStatsHeader(false);
                showFragment(ActiveFragment.PLAYERS);
                loadPlayers();
                break;
            default:
                break;
        }

        // Close the drawer.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onListFragmentInteraction(Game game)
    {
        // Add game event to the calendar.
        Utilities.addGameEventToCalendar(this, game);
    }

    @Override
    public void onListFragmentPlayerInteraction(Player player)
    {
        // Show the selected player details.
        Utilities.showPlayerDetails(this, player);
    }

    /**
     * Load the games from the service and update the fragment list.
     */
    private void loadGames()
    {
        // Make request.
        Call<List<Game>> gamesCall = apiService.getGames();

        // On Response.
        gamesCall.enqueue(new Callback<List<Game>>()
        {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response)
            {
                // If the response was successful and is not empty.
                if(response.isSuccessful() && response.body() != null)
                {
                    // Update the list of games for each league.
                    updateLeagues(response.body());

                    // Update the currently shown list.
                    updateGameList();

                    // Hide the loading indicator.
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t)
            {
                // If the response fails, just hide the loading indicator.
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * Load the statistics from the service and update the fragment list.
     */
    public void loadStatistics()
    {
        // Make request.
        Call<List<Statistic>> statisticsCall = apiService.getStatistics();

        // On Response.
        statisticsCall.enqueue(new Callback<List<Statistic>>()
        {
            @Override
            public void onResponse(Call<List<Statistic>> call, Response<List<Statistic>> response)
            {
                // If the response was successful and is not empty.
                if(response.isSuccessful() && response.body() != null)
                {
                    // Update the statistics list.
                    updateStatisticList(new ArrayList<>(response.body()));

                    // Hide the loading indicator.
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Statistic>> call, Throwable t)
            {
                // If the response fails, just hide the loading indicator.
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * Load the players from the service and update the fragment list.
     */
    public void loadPlayers()
    {
        // Make request.
        Call<List<Player>> playersCall = apiService.getPlayers();

        // On Response.
        playersCall.enqueue(new Callback<List<Player>>()
        {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response)
            {
                // If the response was successful and is not empty.
                if(response.isSuccessful() && response.body() != null)
                {
                    // Update the statistics list.
                    updatePlayerList(new ArrayList<>(response.body()));

                    // Hide the loading indicator.
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t)
            {
                // If the response fails, just hide the loading indicator.
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * Load the corresponding fragment data.
     * @param typeOfFragment Type of fragment.
     */
    private void loadData(ActiveFragment typeOfFragment)
    {
        switch (typeOfFragment)
        {
            case GAMES:
                loadGames();
                break;
            case STATISTICS:
                loadStatistics();
                break;
            case PLAYERS:
                loadPlayers();
                break;
            default:
                break;
        }
    }

    /**
     * Show the indicated fragment.
     * @param typeOfFragment The type of fragment that wants to be shown.
     */
    private void showFragment(ActiveFragment typeOfFragment)
    {
        // Show the loagin indicator.
        swipeRefreshLayout.setRefreshing(true);

        // Set a null fragment.
        Fragment fragment = null;

        // Create the corresponding fragment.
        switch (typeOfFragment)
        {
            case GAMES:
                fragment = GamesFragment.newInstance(new ArrayList<>(games));
                break;
            case STATISTICS:
                fragment = StatisticsFragment.newInstance(statistics);
                break;
            case PLAYERS:
                fragment = PlayersFragment.newInstance(players, Constants.PLAYERS_GRID_COLUMNS);
                break;
            default:
                break;
        }

        // Replace the fragment container with the created fragment.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        // Now the active fragment has changed.
        activeFragment = typeOfFragment;

        // Load the corresponding data.
        loadData(activeFragment);
    }

    /**
     * Update the game list for each league.
     * @param allGames List containing the games of all leagues.
     */
    private void updateLeagues(List<Game> allGames)
    {
        // Clear the lists.
        games.clear();
        copaMx.clear();
        ascensoMx.clear();

        // Update the corresponding leagues.
        for(Game game : allGames)
        {
            if(game.getLeague().equalsIgnoreCase("Copa MX"))
                copaMx.add(game);
            else
                ascensoMx.add(game);
        }

        // Set the fragment games list with the corresponding league.
        TabLayout tabs = findViewById(R.id.tabs);

        if(tabs.getSelectedTabPosition() == 0)
            games = new ArrayList<>(copaMx);
        else
            games = new ArrayList<>(ascensoMx);
    }

    /**
     * Update the list of the Game Fragment.
     */
    private void updateGameList()
    {
        // Get the fragment.
        GamesFragment gamesFragment = (GamesFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        // If games is not null and is added.
        if(gamesFragment != null && gamesFragment.isAdded())
        {
            // Put the updated list on the fragment´s arguments.
            gamesFragment.getArguments().putSerializable(Constants.GAMES_KEY, new ArrayList<>(games));

            // Update the recylcer view.
            gamesFragment.updateGames();
        }
    }

    /**
     * Update the list of the Game Fragment.
     */
    private void updateStatisticList(ArrayList<Statistic> statistics)
    {
        // Get the fragment.
        StatisticsFragment statisticsFragment = (StatisticsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        // If games is not null and is added.
        if(statisticsFragment != null && statisticsFragment.isAdded())
        {
            // Put the updated list on the fragment´s arguments.
            statisticsFragment.getArguments().putSerializable(Constants.STATISTICS_KEY, statistics);

            // Update the recylcer view.
            statisticsFragment.updateStatistics();
        }
    }

    /**
     * Update the list of the Game Fragment.
     */
    private void updatePlayerList(ArrayList<Player> players)
    {
        // Get the fragment.
        PlayersFragment playersFragment = (PlayersFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        // If games is not null and is added.
        if(playersFragment != null && playersFragment.isAdded())
        {
            // Put the updated list on the fragment´s arguments.
            playersFragment.getArguments().putSerializable(Constants.PLAYERS_KEY, players);

            // Update the recylcer view.
            playersFragment.updatePlayers();
        }
    }

    /**
     * Create and get the OnRefreshListener, which will load the corresponding
     * information.
     * @return The OnRefreshListener
     */
    private SwipeRefreshLayout.OnRefreshListener getRefreshListener()
    {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                loadData(activeFragment);
            }
        };
    }

    /**
     * Create and get the OnTabSelectedListener, which will show the games
     * of the corresponding league.
     * @return The OnRefreshListener
     */
    private TabLayout.OnTabSelectedListener getTabListener()
    {
        return new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if(tab.getText().equals("COPA MX"))
                    games = copaMx;
                else
                    games = ascensoMx;

                updateGameList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Leave empty as it is not used.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Leave empty as it is not used.
            }
        };
    }

    /**
     * Show or hide the leagues tabs.
     * @param show Indicates whether to show or hide the tabs.
     */
    private void showTabs(boolean show)
    {
        int visibility = show ? View.VISIBLE : View.GONE;
        findViewById(R.id.tabs).setVisibility(visibility);
    }

    /**
     * Show or hide the statistics header.
     * @param show Indicates whether to show or hide the statistic header.
     */
    private void showStatsHeader(boolean show)
    {
        int visibility = show ? View.VISIBLE : View.GONE;
        findViewById(R.id.stats_header).setVisibility(visibility);
    }
}
