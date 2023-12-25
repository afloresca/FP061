package com.example.ppt_kotders

import com.example.ppt_kotders.models.User
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path


interface FirebaseApiService {

    @GET("usuarios.json")
    suspend fun getJugadores(): Response<Map<String, User>>

    @GET("premios/{premioId}/puntos.json")
    suspend fun getPuntosDePremio(@Path("premioId") premioId: String): Response<Int>
}