package com.example.ppt_kotders.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.example.ppt_kotders.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.ppt_kotders.FirebaseApiService
import com.example.ppt_kotders.databinding.ActivityClasificacionBinding
import com.example.ppt_kotders.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Clasificacion : AppCompatActivity() {

    private lateinit var binding: ActivityClasificacionBinding
    private val listaUsuarios: MutableList<User> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClasificacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btSalir = findViewById<Button>(R.id.btSalir)

        btSalir.setOnClickListener(){
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }

        lifecycleScope.launch {
            try {
                Log.d("DEBUG_TAG", "Iniciando obtenerTopJugadores() al inicio de la actividad")
                obtenerTopJugadores()
                Log.d("DEBUG_TAG", "Obtención de jugadores al inicio completa")

                // Llamamos a ordenarYmostrarJugadores() después de obtener los jugadores
                Log.d("DEBUG_TAG", "Iniciando ordenarYmostrarJugadores()")
                ordenarYmostrarJugadores()
                Log.d("DEBUG_TAG", "Jugadores ordenados y mostrados")

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("DEBUG_TAG", "Error al obtener o mostrar jugadores: $e")
            }
        }
    }

    private suspend fun obtenerTopJugadores() {
        try {
            Log.d("DEBUG_TAG", "Iniciando obtenerTopJugadores()")
            val retrofit = Retrofit.Builder()
                .baseUrl("https://kotders-dbenitez-default-rtdb.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(FirebaseApiService::class.java)

            val response = service.getJugadores()

            if (response.isSuccessful) {
                val responseBody = response.body()

                responseBody?.forEach { (userId, jugador) ->
                    val nuevoJugador = User(jugador.email, jugador.nombre, jugador.puntos, jugador.victorias)
                    listaUsuarios.add(nuevoJugador)
                    Log.d("DEBUG_TAG", "Jugador agregado: $nuevoJugador")
                }
                Log.d("DEBUG_TAG", "Total de jugadores: ${listaUsuarios.size}")

                // Ordenar y mostrar jugadores aquí (puedes llamar a tu función ordenarYmostrarJugadores aquí)
                ordenarYmostrarJugadores()
            } else {
                // Manejar el error de la respuesta no exitosa
                Log.e("DEBUG_TAG", "Error al obtener los datos de Firebase: ${response.code()}")
                throw Exception("Error al obtener los datos de Firebase: ${response.code()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DEBUG_TAG", "Excepción al obtener jugadores: $e")
        }
    }

    private fun ordenarYmostrarJugadores(){
        try {
            // Nos importa de esta lista EMAIL Y PUNTOS
            val listaOrdenada = listaUsuarios.sortedByDescending { it.puntos }

            Log.d("DEBUG_TAG", "Lista ordenada: $listaOrdenada")
            lifecycleScope.launch(Dispatchers.Main) {
                for (i in listaOrdenada.indices) {

                    when (i) {
                        0 -> {
                            if (listaOrdenada[i] != null) {
                                binding.posicion1.text = "1"
                                binding.jugador1.text = listaOrdenada[i].email
                                binding.puntos1.text = listaOrdenada[i].puntos.toString()
                            }
                        }
                        1 -> {
                            if (listaOrdenada[i] != null) {
                                binding.posicion2.text = "2"
                                binding.jugador2.text = listaOrdenada[i].email
                                binding.puntos2.text = listaOrdenada[i].puntos.toString()
                            }
                        }
                        2 -> {
                            if (listaOrdenada[i] != null) {
                                binding.posicion3.text = "3"
                                binding.jugador3.text = listaOrdenada[i].email
                                binding.puntos3.text = listaOrdenada[i].puntos.toString()
                            }
                        }
                        3 -> {
                            if (listaOrdenada[i] != null) {
                                binding.posicion4.text = "4"
                                binding.jugador4.text = listaOrdenada[i].email
                                binding.puntos4.text = listaOrdenada[i].puntos.toString()
                            }
                        }
                        4 -> {
                            if (listaOrdenada[i] != null) {
                                binding.posicion5.text = "5"
                                binding.jugador5.text = listaOrdenada[i].email
                                binding.puntos5.text = listaOrdenada[i].puntos.toString()
                            }
                        }
                        5 -> {
                            if (listaOrdenada[i] != null) {
                                binding.posicion6.text = "6"
                                binding.jugador6.text = listaOrdenada[i].email
                                binding.puntos6.text = listaOrdenada[i].puntos.toString()
                            }
                        }
                        6 -> {
                            if (listaOrdenada[i] != null) {
                                binding.posicion7.text = "7"
                                binding.jugador7.text = listaOrdenada[i].email
                                binding.puntos7.text = listaOrdenada[i].puntos.toString()
                            }
                        }
                        7 -> {
                            if (listaOrdenada[i] != null) {
                                binding.posicion8.text = "8"
                                binding.jugador8.text = listaOrdenada[i].email
                                binding.puntos8.text = listaOrdenada[i].puntos.toString()
                            }
                        }
                        8 -> {
                            if (listaOrdenada[i] != null) {
                                binding.posicion9.text = "9"
                                binding.jugador9.text = listaOrdenada[i].email
                                binding.puntos9.text = listaOrdenada[i].puntos.toString()
                            }
                        }
                        9 -> {
                            if (listaOrdenada[i] != null) {
                                binding.posicion10.text = "10"
                                binding.jugador10.text = listaOrdenada[i].email
                                binding.puntos10.text = listaOrdenada[i].puntos.toString()
                            }
                        }
                        else -> {
                            break
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("DEBUG_TAG", "Excepción al ordenar y mostrar jugadores: $e")
        }
    }
}