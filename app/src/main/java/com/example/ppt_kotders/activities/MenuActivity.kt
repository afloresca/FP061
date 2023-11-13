package com.example.ppt_kotders.activities

import com.example.ppt_kotders.database.MyDBOpenHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton

class MenuActivity : AppCompatActivity() {

    val TAG = "Menu"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu) // layout menu

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
            startActivity(intent)

        }


        jugarBt.setOnClickListener {
            val intent = Intent(this, Juego::class.java)
            intent.putExtra("Jugador_ID", idUser) // Adjuntar el ID del jugador al Intent
            startActivity(intent)

        }

        historicoBt.setOnClickListener {
            val intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)

        }
    }
}