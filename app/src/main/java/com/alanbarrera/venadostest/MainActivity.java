package com.alanbarrera.venadostest;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.alanbarrera.venadostest.fragments.GamesFragment;
import com.alanbarrera.venadostest.interfaces.IVenadosApiService;
import com.alanbarrera.venadostest.models.Game;
import com.alanbarrera.venadostest.network.VenadosApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GamesFragment.OnListFragmentInteractionListener
{
    IVenadosApiService apiService;

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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        apiService = VenadosApiService.getService();
        loadGames();
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
                loadGames();
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
    public void onListFragmentInteraction(Game item) {

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
                    ArrayList<Game> games = new ArrayList<>(response.body());

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, GamesFragment.newInstance(games))
                            .commit();
                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t)
            {
                Log.i("GamesCallback", "Failed to load elements");
            }
        });
    }
}
