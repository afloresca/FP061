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

    private val myDBFirebase = FirebaseDatabase
        .getInstance("https://kotders-dbenitez-default-rtdb.firebaseio.com/")
        .reference

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
                // Obtener la referencia a la lista de usuarios en Firebase
                val usersReference = myDBFirebase.child("usuarios")

                // Agregar un listener para verificar si el correo electrónico ya existe
                usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("onCancelled", "Error!", error.toException())
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val usersList: MutableList<User> =
                            snapshot.getValue(object : GenericTypeIndicator<MutableList<User>>() {})
                                ?: mutableListOf()

                        val emailExists = usersList.any { it.email == UserSingelton.getEmail(this@LoginActivity) }

                        if (!emailExists) {
                            // El correo electrónico no existe, agregar el nuevo usuario
                            val newUser = User(UserSingelton.getEmail(this@LoginActivity), nombreplayer, "0", "0")
                            usersList.add(newUser)
                            Toast.makeText(this@LoginActivity, "Usuario registrado en Firebase", Toast.LENGTH_SHORT).show()

                            // Enviar la lista actualizada a Firebase
                            usersReference.setValue(usersList)

                            // Resto de tu lógica aquí, por ejemplo, iniciar una nueva actividad
                            val id = myDBOpenHelper.getUserID(nombreplayer)
                            val intent = Intent(this@LoginActivity, Menu::class.java)
                            UserSingelton.id = id
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@LoginActivity, "Usuario ya registrado, utiliza otro correo electrónico", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }
}

