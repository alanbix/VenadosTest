package com.alanbarrera.venadostest;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.alanbarrera.venadostest.fragments.GamesFragment;
import com.alanbarrera.venadostest.fragments.PlayersFragment;
import com.alanbarrera.venadostest.fragments.StatisticsFragment;
import com.alanbarrera.venadostest.interfaces.IVenadosApiService;
import com.alanbarrera.venadostest.models.Game;
import com.alanbarrera.venadostest.models.Player;
import com.alanbarrera.venadostest.models.Statistic;
import com.alanbarrera.venadostest.network.VenadosApiService;
import com.alanbarrera.venadostest.utils.CalendarUtil;

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
    IVenadosApiService apiService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Game> games;
    private ArrayList<Game> copaMx;
    private ArrayList<Game> ascensoMx;
    private ArrayList<Statistic> statistics;
    private ArrayList<Player> players;
    private ActiveFragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText(R.string.tab_copa_mx));
        tabs.addTab(tabs.newTab().setText(R.string.tab_ascenso_mx));
        tabs.addOnTabSelectedListener(getTabListener());

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(getRefreshListener());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        apiService = VenadosApiService.getService();
        copaMx = new ArrayList<>();
        ascensoMx = new ArrayList<>();
        games = new ArrayList<>();
        statistics = new ArrayList<>();
        players = new ArrayList<>();
        showGamesFragment();
        activeFragment = ActiveFragment.GAMES;
    }

    @Override
    public void onBackPressed() {
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
        swipeRefreshLayout.setRefreshing(true);

        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_home:
                showTabs(true);
                showStatsHeader(false);
                showGamesFragment();
                break;
            case R.id.nav_statistics:
                showTabs(false);
                showStatsHeader(true);
                showStatisticsFragment();
                break;
            case R.id.nav_players:
                showTabs(false);
                showStatsHeader(false);
                showPlayersFragment();
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Game game)
    {
        CalendarUtil.addGameEventToCalendar(this, game);
    }

    @Override
    public void onListFragmentPlayerInteraction(Player player) {
        Log.i("playerName", player.getName());
    }

    private void loadGames()
    {
        Call<List<Game>> gamesCall = apiService.getGames();

        gamesCall.enqueue(new Callback<List<Game>>()
        {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response)
            {
                if(response.isSuccessful() && response.body() != null)
                {
                    updateLeagues(response.body());
                    updateGameList();

                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t)
            {
                Log.i("GamesCallback", "Failed to load elements");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadStatistics()
    {
        Call<List<Statistic>> statisticsCall = apiService.getStatistics();

        statisticsCall.enqueue(new Callback<List<Statistic>>()
        {
            @Override
            public void onResponse(Call<List<Statistic>> call, Response<List<Statistic>> response)
            {
                if(response.isSuccessful() && response.body() != null)
                {
                    updateStatisticList(new ArrayList<>(response.body()));
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Statistic>> call, Throwable t)
            {
                Log.i("StatisticsCallback", "Failed to load elements");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadPlayers()
    {
        Call<List<Player>> playersCall = apiService.getPlayers();

        playersCall.enqueue(new Callback<List<Player>>()
        {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response)
            {
                if(response.isSuccessful() && response.body() != null)
                {
                    updatePlayerList(new ArrayList<>(response.body()));
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t)
            {
                Log.i("StatisticsCallback", "Failed to load elements");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showGamesFragment()
    {
        activeFragment = ActiveFragment.GAMES;

        swipeRefreshLayout.setRefreshing(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, GamesFragment.newInstance(games))
                .commit();

        loadGames();
    }

    private void showStatisticsFragment()
    {
        activeFragment = ActiveFragment.STATISTICS;

        swipeRefreshLayout.setRefreshing(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, StatisticsFragment.newInstance(statistics))
                .commit();

        loadStatistics();
    }

    private void showPlayersFragment()
    {
        activeFragment = ActiveFragment.PLAYERS;

        swipeRefreshLayout.setRefreshing(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, PlayersFragment.newInstance(players, 3))
                .commit();

        loadPlayers();
    }

    private void updateLeagues(List<Game> allGames)
    {
        games.clear();
        copaMx.clear();
        ascensoMx.clear();

        for(Game game : allGames)
        {
            if(game.getLeague().equalsIgnoreCase("Copa MX"))
                copaMx.add(game);
            else
                ascensoMx.add(game);
        }

        TabLayout tabs = findViewById(R.id.tabs);

        if(tabs.getSelectedTabPosition() == 0)
            games = new ArrayList<>(copaMx);
        else
            games = new ArrayList<>(ascensoMx);
    }

    private void updateGameList()
    {
        GamesFragment gamesFragment = (GamesFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(gamesFragment != null && gamesFragment.isAdded())
        {
            gamesFragment.getArguments().putSerializable("GAMES", games);
            gamesFragment.updateGames();
        }
    }

    private void updateStatisticList(ArrayList<Statistic> statistics)
    {
        StatisticsFragment statisticsFragment = (StatisticsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(statisticsFragment != null && statisticsFragment.isAdded())
        {
            statisticsFragment.getArguments().putSerializable("STATISTICS", statistics);
            statisticsFragment.updateStatistics();
        }
    }

    private void updatePlayerList(ArrayList<Player> players)
    {
        PlayersFragment playersFragment = (PlayersFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(playersFragment != null && playersFragment.isAdded())
        {
            playersFragment.getArguments().putSerializable("PLAYERS", players);
            playersFragment.updatePlayers();
        }
    }

    private SwipeRefreshLayout.OnRefreshListener getRefreshListener()
    {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                switch (activeFragment)
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
        };
    }

    private TabLayout.OnTabSelectedListener getTabListener()
    {
        return new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                if(tab.getText().equals("COPA MX"))
                    games = new ArrayList<>(copaMx);
                else
                    games = new ArrayList<>(ascensoMx);

                updateGameList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

    private void showTabs(boolean show)
    {
        int visibility = show ? View.VISIBLE : View.GONE;
        findViewById(R.id.tabs).setVisibility(visibility);
    }

    private void showStatsHeader(boolean show)
    {
        int visibility = show ? View.VISIBLE : View.GONE;
        findViewById(R.id.stats_header).setVisibility(visibility);
    }

    enum ActiveFragment{GAMES, STATISTICS, PLAYERS}
}
