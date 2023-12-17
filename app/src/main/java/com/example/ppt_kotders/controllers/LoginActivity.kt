package com.example.ppt_kotders.controllers

import MyDBOpenHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ppt_kotders.R
import com.example.ppt_kotders.SavedPreferencesUser
import com.example.ppt_kotders.UserSingelton

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_preferences_user)

        val loginbt = findViewById<Button>(R.id.btlog)
        val inserttxt = findViewById<EditText>(R.id.editTextText2)
        val textview = findViewById<TextView>(R.id.textView8)
        val myDBOpenHelper = MyDBOpenHelper(this, null)

        // Obtener el nombre del objeto SavedPreferences
        val savedUsername = SavedPreferencesUser.getUsername(this)

        // Mostrar el nombre en el EditText
        inserttxt.setText(savedUsername)


        loginbt.setOnClickListener() {

            var nombreplayer = inserttxt.text.toString()

            if (nombreplayer == "") {
                textview.text = getString(R.string.not_valid)
            } else {
                var id = myDBOpenHelper.getUserID(nombreplayer)

                if (id == 0) { // Si existe el 1er jug guarda el id y accede automatico
                    myDBOpenHelper.addPlayer(nombreplayer, 0)
                    // Establecer filtro login
                }

                id = myDBOpenHelper.getUserID(nombreplayer)
                val intent = Intent(this, Menu::class.java)

                UserSingelton.id = id
                startActivity(intent)
            }
    }
}

}