package com.example.ppt_kotders.controllers

import MyDBOpenHelper
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import androidx.compose.animation.core.animate
import com.example.ppt_kotders.MainActivity
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton
import io.reactivex.rxjava3.core.Observable

class Juego : AppCompatActivity() {
    val TAG = "Juego"

    private val opciones = listOf(R.drawable.piedra, R.drawable.papel, R.drawable.tijera)
    private var animacionEnProgreso = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        val MyDBOpenHelper = MyDBOpenHelper(this,null)
        val idUser = UserSingelton.id

        if(idUser == -1){ // LogOut de seguridad
            val intent = Intent(this, MainActivity::class.java)
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
        var rondasMaximas = 50



        fun win(): Observable<Unit> {
            return MyDBOpenHelper.getUser(idUser)
                .flatMap { jugador ->
                    MyDBOpenHelper.addGame(jugador.nombre, "Victoria")
                        .concatWith(MyDBOpenHelper.updatePoints(jugador))
                }
                .doOnComplete {
                    val intent = Intent(this, SolucionJuego::class.java)
                    intent.putExtra("Resultado", 1)
                    intent.putExtra("Jugador_ID", idUser)
                    startActivity(intent)
                }
        }

        fun lose(): Observable<Unit> {
            return MyDBOpenHelper.getUser(idUser)
                .flatMap { jugador ->
                    MyDBOpenHelper.addGame(jugador.nombre, "Derrota")
                }
                .doOnComplete {
                    val intent = Intent(this, SolucionJuego::class.java)
                    intent.putExtra("Jugador_ID", idUser)
                    intent.putExtra("Resultado", 0)
                    startActivity(intent)
                }
        }

        fun determineWinner() {
            if (puntj == 3 || puntm == 3) {
                when {
                    puntj == 3 -> {
                        UserSingelton.estado = 1
                        win().subscribe()
                    }
                    puntm == 3 -> {
                        UserSingelton.estado = 2
                        lose().subscribe()
                    }
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

        fun determineWinnerRound(eleccionMaquina: Int, eleccionJugador: Int):Int {
            var res = 0
            if (rondasJugadas <= rondasMaximas){
                if ((eleccionJugador == 0 && eleccionMaquina == 2) ||
                    (eleccionJugador == 1 && eleccionMaquina == 0) ||
                    (eleccionJugador == 2 && eleccionMaquina == 1)
                ) {
                    // Si gana el jugador
                    EstadoTV.text = getString(R.string.tx_victory)
                    puntj++
                    Log.d(TAG, "El jugador ha ganado")
                    res = 1
                } else if ((eleccionJugador == 0 && eleccionMaquina == 0) ||
                    (eleccionJugador == 1 && eleccionMaquina == 1) ||
                    (eleccionJugador == 2 && eleccionMaquina == 2)){
                    // Si empata el jugador
                    EstadoTV.text = getString(R.string.tx_pair)
                    Log.d(TAG, "El juego ha quedado en empate")
                } else {
                    // Si pierde el jugador
                    EstadoTV.text = getString(R.string.tx_loose)
                    puntm++
                    Log.d(TAG, "El jugador ha perdido")
                    res = 2
                }

                updateCountRound()
                updateCountPlayers()
                determineWinner()
                rondasJugadas++


            }
            return res
        }
        fun iniciarAnimacion() {
            animacionEnProgreso = true

            val handler = Handler(Looper.getMainLooper())
            val delay = 50 // Milisegundos entre cada cambio de imagen

            handler.postDelayed(object : Runnable {
                override fun run() {
                    imgMaquina.setImageResource(opciones.random())
                    if (animacionEnProgreso) {
                        handler.postDelayed(this, delay.toLong())
                    }
                }
            }, delay.toLong())
        }
        fun detenerAnimacion() {
            animacionEnProgreso = false
            PiedraBT.isEnabled = false
            PapelBT.isEnabled = false
            TijerasBTT.isEnabled = false
        }
        fun reiniciar() {
            Handler(Looper.getMainLooper()).postDelayed({
                iniciarAnimacion()
                imgJugador.setImageResource(R.drawable.incognito)
                EstadoTV.text = ""
                PiedraBT.isEnabled = true
                PapelBT.isEnabled = true
                TijerasBTT.isEnabled = true
            }, 3000)

        }

        PiedraBT.setOnClickListener {
            val result = playMachine()
            detenerAnimacion()
            Handler(Looper.getMainLooper()).postDelayed({
            imgJugador.setImageResource(R.drawable.piedra)
            imgMaquina.setImageResource(getDrawableResource(result))
            var res = determineWinnerRound(result,0)
            reiniciar()
            }, 100)
        }
        PapelBT.setOnClickListener {
            val result = playMachine()
            detenerAnimacion()
            Handler(Looper.getMainLooper()).postDelayed({
            imgJugador.setImageResource(R.drawable.papel)
            imgMaquina.setImageResource(getDrawableResource(result))
            var res =determineWinnerRound(result,1)
            reiniciar()
            }, 100)

        }
        TijerasBTT.setOnClickListener {
            val result = playMachine()
            detenerAnimacion()
            Handler(Looper.getMainLooper()).postDelayed({
            imgJugador.setImageResource(R.drawable.tijera)
            imgMaquina.setImageResource(getDrawableResource(result))
            var res = determineWinnerRound(result,2)
            reiniciar()
            }, 100)
        }

        salir.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            intent.putExtra("Jugador_ID",idUser)
            startActivity(intent)
        }



        iniciarAnimacion() // Empieza la magia
    }

}