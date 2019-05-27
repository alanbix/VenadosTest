package com.alanbarrera.venadostest;

import android.os.Bundle;
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

import com.alanbarrera.venadostest.fragments.GamesFragment;
import com.alanbarrera.venadostest.interfaces.IVenadosApiService;
import com.alanbarrera.venadostest.models.Game;
import com.alanbarrera.venadostest.network.VenadosApiService;
import com.alanbarrera.venadostest.utils.CalendarUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GamesFragment.OnListFragmentInteractionListener
{
    IVenadosApiService apiService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Game> games;
    private ArrayList<Game> copaMx;
    private ArrayList<Game> ascensoMx;

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
        showGamesFragment();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_home:
                showGamesFragment();
                break;
            case R.id.nav_statistics:
                break;
            case R.id.nav_players:
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
                    updateFragmentList();

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

    private void showGamesFragment()
    {
        swipeRefreshLayout.setRefreshing(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, GamesFragment.newInstance(games))
                .commit();

        loadGames();
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

    private void updateFragmentList()
    {
        GamesFragment gamesFragment = (GamesFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(gamesFragment != null && gamesFragment.isAdded())
        {
            gamesFragment.getArguments().putSerializable("GAMES", games);
            gamesFragment.updateGames();
        }
    }

    private SwipeRefreshLayout.OnRefreshListener getRefreshListener()
    {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadGames();
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

                updateFragmentList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }
}
