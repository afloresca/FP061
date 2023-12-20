package com.example.ppt_kotders.models


import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PremioDB {
    val premio : Premio? = null
    val database = Firebase.database("https://piedrapapeltijeraskot-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("premio")
}