package com.example.ppt_kotders

import MyDBOpenHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Juego : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.juego)

        val MyDBOpenHelper = MyDBOpenHelper(this,null)
        val idUser = intent.getIntExtra("Jugador_ID",-1)
        if(idUser == -1){ // LogOut de seguridad
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        val jugador = MyDBOpenHelper.getUser(idUser)


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
            var result = 0

            contadortv.text = idUser.toString()

            PiedraBT.setOnClickListener {


            }
            PapelBT.setOnClickListener {

                MyDBOpenHelper.addGame(jugador.nombre.toString(),"Derrota")
                val intent = Intent(this,SolucionJuego::class.java)
                intent.putExtra("Resultado",0)
                intent.putExtra("Jugador_ID",idUser)
                startActivity(intent)

                //imgJugador.setImageResource(R.drawable.papel)
                //imgMaquina.setImageResource(playMachine())
            }
            TijerasBTT.setOnClickListener {
                imgJugador.setImageResource(R.drawable.tijera)
                imgMaquina.setImageResource(playMachine())
            }

            salir.setOnClickListener {
                val intent = Intent(this,Menu::class.java)
                intent.putExtra("Jugador_ID",idUser)
                startActivity(intent)
            }

        fun win(){ // Si el jugador gana añade las monedas y pasa al layout de victoria
            // Registra partida
            MyDBOpenHelper.addGame(jugador.nombre.toString(),"Victoria")
            MyDBOpenHelper.updatePoints(jugador)

            val intent = Intent(this,SolucionJuego::class.java)
            intent.putExtra("Resultado",1)
            intent.putExtra("Jugador_ID",idUser)
            startActivity(intent)
        }

        fun lose(){
            MyDBOpenHelper.addGame(jugador.nombre.toString(),"Derrota")
            val intent = Intent(this,SolucionJuego::class.java)
            intent.putExtra("Jugador_ID",idUser)
            intent.putExtra("Resultado",0)
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