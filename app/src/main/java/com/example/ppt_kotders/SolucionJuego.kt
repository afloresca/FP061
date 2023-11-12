package com.example.ppt_kotders

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SolucionJuego : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idUser = intent.getIntExtra("Jugador_ID",-1)
        val condicion = intent.getStringExtra("Resultado")

        // Condicionantes de logout
        if(condicion == null || idUser == -1){
            if(idUser == -1){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            } else{
                val intent = Intent(this,Menu::class.java)
                intent.putExtra("Jugador_ID",idUser)
                startActivity(intent)
            }
        }

        val buttonD = findViewById<Button>(R.id.buttonD)
        val buttonV = findViewById<Button>(R.id.buttonV)

        if(condicion == "v") {
            setContentView(R.layout.activity_win)
            buttonV.setOnClickListener(){

                val intent = Intent(this,Menu::class.java)
                intent.putExtra("Jugador_ID",idUser)
                startActivity(intent)
            }
            // Declarar contenidos
        }

        if(condicion == "d"){
            setContentView(R.layout.activity_lose)

            val intent = Intent(this,Menu::class.java)
            intent.putExtra("Jugador_ID",idUser)
            startActivity(intent)

        }

    }
}