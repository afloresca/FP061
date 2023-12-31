package com.example.ppt_kotders

import com.example.ppt_kotders.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path


interface FirebaseApiService {

    @GET("usuarios.json")
    suspend fun getJugadores(): Response<Map<String, User>>

    @GET("premios/1/puntos.json")
    suspend fun getPremio(): Response<Int>

    @PUT("premios/1/puntos.json")
    suspend fun putPremio(@Body value: Int): Response<Void>

    @GET("usuarios/{uid}/puntos.json")
    suspend fun getPuntosUser(@Path("uid") uid: String): Response<User>

    @PUT("usuarios/{uid}/puntos.json")
    suspend fun actualizarPuntosUsuario(@Path("uid") uid: String, @Body value: Int): Response<Void>
}