package com.alanbarrera.venadostest.interfaces;

import com.alanbarrera.venadostest.models.Game;
import com.alanbarrera.venadostest.models.Player;
import com.alanbarrera.venadostest.models.Statistic;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface IVenadosApiService
{
    @Headers("Accept: application/json")
    @GET("games")
    Call<List<Game>> getGames();

    @Headers("Accept: application/json")
    @GET("statistics")
    Call<List<Statistic>> getStatistics();

    @Headers("Accept: application/json")
    @GET("players")
    Call<List<Player>> getPlayers();
}
