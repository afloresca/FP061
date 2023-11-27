package com.example.ppt_kotders.controllers

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ppt_kotders.R

class NotiVictoria : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.noti_victoria)

        val buttonD = findViewById<Button>(R.id.buttonD)
        val imagen = findViewById<ImageView>(R.id.imageView)
        val texto = findViewById<TextView>(R.id.textVictoria)

        val tiempoResultado = getIntent().extras?.getInt("tiempoResultado")


        imagen.setImageResource(R.drawable.ppt)
        texto.text = " VICTORIA en $tiempoResultado Segundos"


        buttonD.setOnClickListener() {
            finish()
        }
    }

}





