package com.example.ppt_kotders

import MyDBOpenHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class Juego : AppCompatActivity() {
    val TAG = "Juego"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.juego)

        val MyDBOpenHelper = MyDBOpenHelper(this,null)
        val idUser = UserSingelton.id

        if(idUser == -1){ // LogOut de seguridad
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            return
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
        var rondasJugadas = 1
        var rondasMaximas = 10

        //contadortv.text = idUser.toString()

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

        fun determineWinner(){
            if (puntj == 3 || puntm == 3) {
                when {
                    puntj == 3 -> win()
                    puntm == 3 -> lose()
                }
            }
        }

        fun updateCountRound(){
            contadortv.text = rondasJugadas.toString()
            Log.d("$TAG", "Se ha actualizado el contador de rondas")
        }

        fun updateCountPlayers(){
            ContJtv.text = puntj.toString()
            Log.d("$TAG", "Se ha actualizado el contador de victorias del jugador")
            ContMtv.text = puntm.toString()
            Log.d("$TAG", "Se ha actualizado el contador de victorias de la máquina")


        }

        fun playMachine(): Int{
            return (0..2).random()
        }

        fun getDrawableResource(result: Int) : Int {
            Log.d("$TAG", "Se ha actualizado la elección del gesto")
            return when (result) {
                0 -> R.drawable.piedra
                1 -> R.drawable.papel
                2 -> R.drawable.tijera
                else -> R.drawable.incognito
            }
        }

        fun determineWinnerRound(eleccionMaquina: Int, eleccionJugador: Int) {
            if (rondasJugadas <= rondasMaximas){
                if ((eleccionJugador == 0 && eleccionMaquina == 2) ||
                    (eleccionJugador == 1 && eleccionMaquina == 0) ||
                    (eleccionJugador == 2 && eleccionMaquina == 1)
                ) {
                    // Si gana el jugador
                    EstadoTV.text = "VICTORIA"
                    puntj++
                    Log.d(TAG, "El jugador ha ganado")
                } else if ((eleccionJugador == 0 && eleccionMaquina == 0) ||
                    (eleccionJugador == 1 && eleccionMaquina == 1) ||
                    (eleccionJugador == 2 && eleccionMaquina == 2)){
                    // Si empata el jugador
                    EstadoTV.text = "EMPATE"
                    Log.d(TAG, "El juego ha quedado en empate")
                } else {
                    // Si pierde el jugador
                    EstadoTV.text = "DERROTA"
                    puntm++
                    Log.d(TAG, "El jugador ha perdido")
                }

                updateCountRound()
                updateCountPlayers()
                determineWinner()
                rondasJugadas++
            }
        }

        PiedraBT.setOnClickListener {
            val result = playMachine()
            imgJugador.setImageResource(R.drawable.piedra)
            imgMaquina.setImageResource(getDrawableResource(result))
            determineWinnerRound(result,0)
        }
        PapelBT.setOnClickListener {
            val result = playMachine()
            imgJugador.setImageResource(R.drawable.papel)
            imgMaquina.setImageResource(getDrawableResource(result))
            determineWinnerRound(result,1)
        }
        TijerasBTT.setOnClickListener {
            val result = playMachine()
            imgJugador.setImageResource(R.drawable.tijera)
            imgMaquina.setImageResource(getDrawableResource(result))
            determineWinnerRound(result,2)
        }

        salir.setOnClickListener {
            val intent = Intent(this,Menu::class.java)
            intent.putExtra("Jugador_ID",idUser)
            startActivity(intent)
        }
    }

}