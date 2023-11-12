package com.example.ppt_kotders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SolucionJuego : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idUser = intent.getIntExtra("Jugador_ID",-1)
        val condicion = intent.getStringExtra("Resultado")
        if(condicion == null || idUser == -1){
        }
        setContentView(R.layout.activity_win)
    }
}