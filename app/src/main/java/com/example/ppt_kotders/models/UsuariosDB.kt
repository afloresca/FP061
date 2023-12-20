package com.example.ppt_kotders.models


import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UsuariosDB {
    val usuarios : Usuarios? = null
    val database = Firebase.database("https://piedrapapeltijeraskot-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("usuarios")
}