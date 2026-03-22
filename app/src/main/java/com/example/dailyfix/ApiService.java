package com.example.dailyfix;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    // Route pour la connexion (POST /api/login)
    @POST("/auth/login")
    Call<LoginResponse> loginUser(@Body User user);

    // Route pour l'inscription (POST /api/register)
    @POST("/auth/register")
    Call<RegisterResponse> registerUser(@Body User user);
}
