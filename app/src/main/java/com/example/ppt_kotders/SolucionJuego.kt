package com.example.ppt_kotders

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SolucionJuego : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_solucion)

        val idUser = UserSingelton.id
        val condicion = UserSingelton.estado
        val buttonD = findViewById<Button>(R.id.buttonD)
        val imagen = findViewById<ImageView>(R.id.imageView)
        val texto = findViewById<TextView>(R.id.textView3)
        val textodata = findViewById<TextView>(R.id.textView)
        textodata.text = idUser.toString()

        if (idUser == -1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        when (condicion) {

            1 -> {
                imagen.setImageResource(R.drawable.ppt)
                texto.text = " VICTORIA "
            }

            2 -> {
                imagen.setImageResource(R.drawable.historico_1_photoroom_png_photoroom)
                texto.text = " DERROTA "
            }

            0 -> logout() // Por defecto -> error del juego
        }

        buttonD.setOnClickListener() {

            val intent = Intent(this, Menu::class.java)
            UserSingelton.estado = 0 // Lo ponemos po defecto
            startActivity(intent)
            finish()

        }
    }

    fun logout(){ // Detecta el estado default y rectifica el error logout
        val intent = Intent(this,MainActivity::class.java)
        UserSingelton.id = 0
        UserSingelton.estado = 0
        startActivity(intent)
        finish()

    }

}