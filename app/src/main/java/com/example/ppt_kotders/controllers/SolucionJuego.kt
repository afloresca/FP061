package com.example.ppt_kotders.controllers

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ppt_kotders.MainActivity
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton

class SolucionJuego : AppCompatActivity() {
    var lista = arrayOfNulls<MediaPlayer>(size = 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_solucion)

        lista[0]=MediaPlayer.create(this,R.raw.fireworks)
        lista[1]=MediaPlayer.create(this,R.raw.trueno)

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
                texto.text = getString(R.string.tx_victory)
                lista[0]?.start()
            }

            2 -> {
                imagen.setImageResource(R.drawable.historico_1_photoroom_png_photoroom)
                texto.text = getString(R.string.tx_loose)
                lista[1]?.start()
            }

            0 -> logout() // Por defecto -> error del juego
        }

        buttonD.setOnClickListener() {

            val intent = Intent(this, Menu::class.java)
            UserSingelton.estado = 0 // Lo ponemos por defecto
            lista[0]?.stop()
            lista[1]?.stop()
            startActivity(intent)
            finish()

        }
    }

    fun logout(){ // Detecta el estado default y rectifica el error logout
        val intent = Intent(this, MainActivity::class.java)
        UserSingelton.id = 0
        UserSingelton.estado = 0
        startActivity(intent)
        finish()

    }

}




