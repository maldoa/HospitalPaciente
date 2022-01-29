package com.cic.caretapaciente;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class DatosRepository {
    private static DatosRepository instance;

    private DatosService datosService;

    public static DatosRepository getInstance() {
        if (instance == null) {
            instance = new DatosRepository();
        }
        return instance;
    }

    public DatosRepository() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://67.205.112.170/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        datosService = retrofit.create(DatosService.class);
    }

    public DatosService getCommentsService() {
        return datosService;
    }
}