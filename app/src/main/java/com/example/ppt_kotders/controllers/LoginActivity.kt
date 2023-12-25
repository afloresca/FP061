package com.example.ppt_kotders.controllers

import MyDBOpenHelper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton
import com.example.ppt_kotders.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginbt = findViewById<Button>(R.id.btlog)
        val inserttxt = findViewById<EditText>(R.id.editTextText2)
        val textview = findViewById<TextView>(R.id.textView8)
        val myDBOpenHelper = MyDBOpenHelper(this, null)

        // Obtener el nombre del objeto SavedPreferences
        val savedUsername = UserSingelton.getUsername(this)

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

