package com.example.ppt_kotders

import com.example.ppt_kotders.models.User
import retrofit2.http.GET
import retrofit2.Response


interface FirebaseApiService {

    @GET("usuarios.json")
    suspend fun getJugadores(): Response<Map<String, User>>
}