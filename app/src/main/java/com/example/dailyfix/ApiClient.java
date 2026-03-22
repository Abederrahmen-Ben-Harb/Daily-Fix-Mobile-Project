package com.example.dailyfix;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    // TODO: Remplacez par l'URL de votre API Node.js (ex: http://192.168.1.10:3000/)
    // Si vous testez sur l'émulateur Android, utilisez 10.0.2.2 au lieu de localhost
    // IMPORTANT: L'URL doit se terminer par / pour que Retrofit concatène correctement les chemins
    private static final String BASE_URL = "https://dailyfix-backend.onrender.com/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
