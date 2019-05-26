package com.alanbarrera.venadostest.network;

import android.util.Log;

import com.alanbarrera.venadostest.interfaces.IVenadosApiService;
import com.alanbarrera.venadostest.models.Game;
import com.alanbarrera.venadostest.models.Player;
import com.alanbarrera.venadostest.models.Statistic;
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
    private static IVenadosApiService venadosService = null;

    public static IVenadosApiService getService()
    {
        if(venadosService == null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .client(getHttpClient())
                    .build();

            venadosService = retrofit.create(IVenadosApiService.class);
        }

        return venadosService;
    }
    
    private static Gson getGson()
    {
        return new GsonBuilder()
                .registerTypeAdapter(List.class, new DataDeserializer())
                .create();
    }

    private static OkHttpClient getHttpClient()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }
}
