package com.example.ppt_kotders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

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

            val imgJugador = findViewById<ImageView>(R.id.imgjugador) // Continene las lecciones
            val imgMaquina = findViewById<ImageView>(R.id.imgmaquina)

            var puntj = 0 // Establecemos los puntuajes a 0
            var puntm = 0



            salir.setOnClickListener {
                val intent = Intent(this,Menu::class.java)
                startActivity(intent)
            }
    }
}