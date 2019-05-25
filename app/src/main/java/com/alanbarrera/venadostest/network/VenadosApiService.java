package com.alanbarrera.venadostest.network;

import android.util.Log;

import com.alanbarrera.venadostest.interfaces.IVenadosApiService;
import com.alanbarrera.venadostest.models.Game;
import com.alanbarrera.venadostest.models.Player;
import com.alanbarrera.venadostest.models.Statistic;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VenadosApiService
{
    private static final String BASE_URL = "https://venados.dacodes.mx/api/";
    private Retrofit retrofit;
    private IVenadosApiService venadosService;

    private List<Game> games;
    private List<Statistic> statistics;
    private List<Player> players;

    public VenadosApiService()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(getHttpClient())
                .build();

        venadosService = retrofit.create(IVenadosApiService.class);
    }

    public List<Game> getGames()
    {
        Call<List<Game>> gamesCall = venadosService.getGames();

        gamesCall.enqueue(new Callback<List<Game>>()
        {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response)
            {
                if(response.isSuccessful())
                {
                    games = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t)
            {
                Log.i("GamesCallback", "Failed to load elements");
            }
        });
        return games;
    }

    public List<Statistic> getStatistics()
    {
        Call<List<Statistic>> statisticsCall = venadosService.getStatistics();

        statisticsCall.enqueue(new Callback<List<Statistic>>()
        {
            @Override
            public void onResponse(Call<List<Statistic>> call, Response<List<Statistic>> response)
            {
                if(response.isSuccessful())
                {
                    statistics = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Statistic>> call, Throwable t)
            {
                Log.i("StatisticsCallback", "Failed to load elements");
            }
        });
        return statistics;
    }

    public List<Player> getPlayers()
    {
        final Call<List<Player>> playersCall = venadosService.getPlayers();

        playersCall.enqueue(new Callback<List<Player>>()
        {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response)
            {
                if(response.isSuccessful())
                {
                        players = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t)
            {
                Log.i("PlayersCallback", "Failed to load elements");
            }
        });
        return players;
    }

    private Gson getGson()
    {
        return new GsonBuilder()
                .registerTypeAdapter(List.class, new DataDeserializer())
                .create();
    }

    private OkHttpClient getHttpClient()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }
}
