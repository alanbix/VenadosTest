package com.alanbarrera.venadostest.network;

import com.alanbarrera.venadostest.interfaces.IVenadosApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VenadosApiService
{
    private static final String BASE_URL = "https://venados.dacodes.mx/api/";
    private static IVenadosApiService venadosService = null;

    /**
     * Get the service (singleton)
     * @return The Api service
     */
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

    /**
     * Get the Gson
     * @return The Gson
     */
    private static Gson getGson()
    {
        // Create and return the Gson with the adapter TypeAdapter attached
        return new GsonBuilder()
                .registerTypeAdapter(List.class, new DataDeserializer())
                .create();
    }

    /**
     * Get the HttpClient
     * @return The OkHttpClient
     */
    private static OkHttpClient getHttpClient()
    {
        // Create a logger.
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Build and return the client with the logger attached.
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }
}
