package com.example.ppt_kotders

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class Juego : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.juego)

            // Bottones jugador
            val PiedraBT = findViewById<ImageButton>(R.id.btpiedra)
            val PapelBT = findViewById<ImageButton>(R.id.btpapel)
            val TijerasBTT = findViewById<ImageButton>(R.id.bttijeras)
            val salir = findViewById<Button>(R.id.btSalir)

            val contadortv = findViewById<TextView>(R.id.contadortv)
            val EstadoTV = findViewById<TextView>(R.id.estadotv) // Victoria o Derrota punto
            val ContJtv = findViewById<TextView>(R.id.txtu) // Contador Puntos jugador
            val ContMtv = findViewById<TextView>(R.id.txmaquina) // Contador Puntos maquina

            var imgJugador = findViewById<ImageView>(R.id.imgjugador) // Continene las lecciones
            var imgMaquina = findViewById<ImageView>(R.id.imgmaquina)

            var puntj = 0 // Establecemos los puntuajes a 0
            var puntm = 0
            var turnoMaquina = false
            var result = 0

            PiedraBT.setOnClickListener {
                imgJugador.setImageResource(R.drawable.piedra)
                imgMaquina.setImageResource(playMachine())
            }
            PapelBT.setOnClickListener {
                imgJugador.setImageResource(R.drawable.papel)
                imgMaquina.setImageResource(playMachine())
            }
            TijerasBTT.setOnClickListener {
                imgJugador.setImageResource(R.drawable.tijera)
                imgMaquina.setImageResource(playMachine())
            }

            salir.setOnClickListener {
                val intent = Intent(this,Menu::class.java)
                startActivity(intent)
            }
    }
}

fun playMachine() : Int {
    val result = (1..3).random()
    return when (result) {
        1 -> R.drawable.papel
        2 -> R.drawable.piedra
        3 -> R.drawable.tijera
        else -> R.drawable.incognito
    }
}