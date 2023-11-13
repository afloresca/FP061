package com.example.ppt_kotders.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton

class SolucionJuego : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

           setContentView(R.layout.activity_solucion)

          val idUser = UserSingelton.id
          val condicion = intent.getIntExtra("Resultado",-1)

          val buttonD = findViewById<Button>(R.id.buttonD)
          val imagen = findViewById<ImageView>(R.id.imageView)
          val texto = findViewById<TextView>(R.id.textView3)
          val textodata = findViewById<TextView>(R.id.textView)
              textodata.text = idUser.toString()

            if(idUser == -1){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            if(condicion == 1) { // Victoria

                imagen.setImageResource(R.drawable.ppt)
                texto.text = " VICTORIA "

            } else{

                imagen.setImageResource(R.drawable.historico_1_photoroom_png_photoroom)
                texto.text = " DERROTA "
            }

            buttonD.setOnClickListener(){

                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("Jugador_Id",idUser)
                startActivity(intent)

        }






        val menuIntent = Intent(this, MenuActivity::class.java)
        menuIntent.putExtra("Jugador_ID",idUser)


    }
}