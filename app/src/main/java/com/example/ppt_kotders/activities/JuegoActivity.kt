package com.example.ppt_kotders.activities

import com.example.ppt_kotders.database.MyDBOpenHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton

class JuegoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_juego)

        val MyDBOpenHelper = MyDBOpenHelper(this,null)
        val idUser = UserSingelton.id

        if (idUser == -1) { // LogOut de seguridad
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cerrar la actividad actual
            return
        }
        val jugador = MyDBOpenHelper.getUser(idUser)


            // Botones Jugador
            val PiedraBT = findViewById<ImageButton>(R.id.btpiedra)
            val PapelBT = findViewById<ImageButton>(R.id.btpapel)
            val TijerasBTT = findViewById<ImageButton>(R.id.bttijeras)
            val salir = findViewById<Button>(R.id.btSalir)

            val contadortv = findViewById<TextView>(R.id.contadortv)
            val EstadoTV = findViewById<TextView>(R.id.estadotv) // Victoria o Derrota Punto
            val ContJtv = findViewById<TextView>(R.id.txtu) // Contador Puntos Jugador
            val ContMtv = findViewById<TextView>(R.id.txmaquina) // Contador Puntos Maquina

            var imgJugador = findViewById<ImageView>(R.id.imgjugador) // Continene los gestos de los jugadores
            var imgMaquina = findViewById<ImageView>(R.id.imgmaquina)

            var puntj = 0 // Establecemos los puntuajes a 0
            var puntm = 0
            var rondasJugadas = 0

            contadortv.text = idUser.toString()

        fun win(){ // Si el jugador gana añade las monedas y pasa al layout de victoria
            // Registra partida
            MyDBOpenHelper.addGame(jugador.nombre.toString(),"Victoria")
            MyDBOpenHelper.updatePoints(jugador)

            val intent = Intent(this, SolucionJuego::class.java)
            intent.putExtra("Resultado",1)
            intent.putExtra("Jugador_ID",idUser)
            startActivity(intent)
        }

        fun lose(){
            MyDBOpenHelper.addGame(jugador.nombre.toString(),"Derrota")
            val intent = Intent(this, SolucionJuego::class.java)
            intent.putExtra("Jugador_ID",idUser)
            intent.putExtra("Resultado",0)
            startActivity(intent)
        }


        // Función para obtener el resultado
        fun getResult():String {
            return if (imgJugador == imgMaquina)
                "EMPATE"
            else if (imgJugador == PiedraBT && imgMaquina == TijerasBTT ||
                     imgJugador == PapelBT && imgMaquina == PiedraBT ||
                     imgJugador == TijerasBTT && imgMaquina == PapelBT)
                "VICTORIA"
            else
                "DERROTA"
        }

        // Función para el conteo de puntos
        // Se ha establecido que se gana al mejor de tres
        fun setScore(){
            if(getResult() == "EMPATE") {
                puntj += 0
                puntm += 0
            }
            else if (getResult() == "VICTORIA") {
                puntj += 1
            }
            else if (getResult() == "DERROTA") {
                puntm += 1
            }
        }



        // Función para actualizar los TextView y mostrar por pantalla
        fun updateTextViews() {
            ContJtv.text = puntj.toString()
            ContMtv.text = puntm.toString()
            ContJtv.text = rondasJugadas.toString()
            ContMtv.text = rondasJugadas.toString()
            EstadoTV.text = getResult()
        }

        // Función para verificar la cantidad de rondas
        fun checkGameResult() {
            if (rondasJugadas < 3) {
                if(puntj == 2){
                    win()
                } else if (puntm == 2){
                    lose()
                }
            }
        }




        PiedraBT.setOnClickListener {
            imgJugador.setImageResource(R.drawable.piedra)
            imgMaquina.setImageResource(playMachine())
            setScore()
            updateTextViews()
            checkGameResult()
        }

        PapelBT.setOnClickListener {
            imgJugador.setImageResource(R.drawable.papel)
            imgMaquina.setImageResource(playMachine())
            setScore()
            updateTextViews()
            checkGameResult()
        }

        TijerasBTT.setOnClickListener {
            imgJugador.setImageResource(R.drawable.tijera)
            imgMaquina.setImageResource(playMachine())
            setScore()
            updateTextViews()
            checkGameResult()
        }

            salir.setOnClickListener {
                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("Jugador_ID",idUser)
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