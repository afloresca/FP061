package com.example.ppt_kotders

import MyDBOpenHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu) // layout menu

        val jugarBt = findViewById<Button>(R.id.btjugar)
        val historicoBt = findViewById<Button>(R.id.bthistorico)
        val nombre = findViewById<TextView>(R.id.textNombre)
        val puntos = findViewById<TextView>(R.id.textPuntos)

        val MyDBOpenHelper = MyDBOpenHelper(this, null)
        var idUser = UserSingelton.id
        var jugador = MyDBOpenHelper.getUser(idUser)
        nombre.text = jugador.nombre.toString()
        puntos.text = jugador.puntuacion.toString()


        if (idUser == -1) { // LogOut de seguridad
            val intent = Intent(this, MainActivity::class.java)
            UserSingelton.id = 0
            startActivity(intent)
            finish()

        }


        jugarBt.setOnClickListener {
            val intent = Intent(this, Juego::class.java)
            startActivity(intent)
            finish()

        }

        historicoBt.setOnClickListener {
            val intent = Intent(this, Historico::class.java)
            startActivity(intent)
            finish()

        }
    }
}