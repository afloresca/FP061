package com.example.ppt_kotders.controllers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

class RealtimeDB {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference.child("usuarios")

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("hhttps://piedrapapeltijeraskot-default-rtdb.europe-west1.firebasedatabase.app/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val userService: UserService = retrofit.create(UserService::class.java)

    //val nuevoUsuario = User(nombre = "NombrePredeterminado", puntos = 0, victorias = 0)

    interface UserService {
        @GET("usuarios/{email}.json")
        fun getUser(@Path("email") userEmail: String): Call<User>

        @PUT("usuarios/{email}.json")
        fun updateUser(@Path("email") userEmail: String, @Body user: User): Call<Void>

        @POST("usuarios/{email}.json")
        fun createUser(@Path("email") userEmail: String, @Body user: com.example.ppt_kotders.models.User): Call<Void>
    }

    fun getUser(userEmail: String, callback: (User?) -> Unit) {
        userService.getUser(userEmail).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                callback(if (response.isSuccessful) response.body() else null)
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun updateUser(userEmail: String, user: User, callback: () -> Unit) {
        userService.updateUser(userEmail, user).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Manejar error de red
            }
        })
    }

    fun createUser(userEmail: String, user: com.example.ppt_kotders.models.User, callback: () -> Unit) {
        userService.createUser(userEmail, user).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Manejar error de red
            }
        })
    }

}