package com.example.ppt_kotders.controllers

import MyDBOpenHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton
import com.example.ppt_kotders.adapter.ItemAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class Historico : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)
        initRecycleView()

        val btSalir = findViewById<Button>(R.id.btSalir)



        btSalir.setOnClickListener(){
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initRecycleView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycleItem)
        val myDBOpenHelper = MyDBOpenHelper(this, null)
        val idUser = UserSingelton.id

        myDBOpenHelper.listPlayerGames(idUser)
            .observeOn(AndroidSchedulers.mainThread()) // Cambia al hilo principal para actualizar la interfaz de usuario
            .subscribe(
                { list ->
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = ItemAdapter(list)
                },
                { error ->
                    // Maneja el error, por ejemplo, mostrando un mensaje al usuario
                    Toast.makeText(this, "Error al obtener el historial de juegos: $error", Toast.LENGTH_SHORT).show()
                }
            )
    }

    private fun crearpartidas() {
        val myDBOpenHelper = MyDBOpenHelper(this, null)
        val idUser = UserSingelton.id

        myDBOpenHelper.getUser(idUser)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { jugador ->
                    if (jugador != null) {
                        myDBOpenHelper.addGame(jugador.nombre, "Victoria")
                    } else {
                        // Maneja el caso en el que jugador es nulo, por ejemplo, mostrando un mensaje
                        Toast.makeText(this, "Error: No se pudo obtener el jugador", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    // Maneja el error, por ejemplo, mostrando un mensaje al usuario
                    Toast.makeText(this, "Error al obtener el jugador: $error", Toast.LENGTH_SHORT).show()
                }
            )
    }
}