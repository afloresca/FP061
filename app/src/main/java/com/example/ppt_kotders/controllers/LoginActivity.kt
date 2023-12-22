package com.example.ppt_kotders.controllers

import MyDBOpenHelper
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton
import com.example.ppt_kotders.models.User

class LoginActivity : AppCompatActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_preferences_user)

        val loginbt = findViewById<Button>(R.id.btlog)
        val inserttxt = findViewById<EditText>(R.id.editTextText2)
        val textview = findViewById<TextView>(R.id.textView8)
        val myDBOpenHelper = MyDBOpenHelper(this, null)
        val realtimeDB = RealtimeDB()

        // Obtener el nombre del objeto SavedPreferences
        val savedUsername = UserSingelton.getUsername(this)

        // Mostrar el nombre en el EditText
        inserttxt.setText(savedUsername)


        loginbt.setOnClickListener() {

            var nombreplayer = inserttxt.text.toString()
            var email = UserSingelton.EMAIL
            var id = 0;

            if (nombreplayer == "") {
                textview.text = getString(R.string.not_valid)
            } else {

                realtimeDB.getUser(email) { user ->
                    if (user != null) {  // Existe en RtimeBD
                        id = myDBOpenHelper.getUserID(nombreplayer)
                        if (id == 0) {  // Creamos en local
                            val puntos = user.puntos
                            myDBOpenHelper.addPlayer(nombreplayer, email,puntos)
                            id = myDBOpenHelper.getUserID(nombreplayer)

                        } else{ // Cogemos el id del jugador
                            id = myDBOpenHelper.getUserID(nombreplayer)
                        }

                    } else { // No existe en RtDB

                        val nom = UserSingelton.USERNAME

                        val nuevoUsuario = User(nombre = nom, puntos = 0, victorias = 0)
                        realtimeDB.createUser(email,nuevoUsuario)
                        }
                    }

                var id = myDBOpenHelper.getUserID(nombreplayer)




                val intent = Intent(this, Menu::class.java)

                UserSingelton.id = id
                startActivity(intent)
            }
    }
}

}