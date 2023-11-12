package com.example.ppt_kotders

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu) // layout menu

        val jugarBt = findViewById<Button>(R.id.btjugar)
        val historicoBt = findViewById<Button>(R.id.bthistorico)

        jugarBt.setOnClickListener{
            val jugando = Intent(this,Juego::class.java)
            startActivity(jugando)
        }

        historicoBt.setOnClickListener {


        }}
}