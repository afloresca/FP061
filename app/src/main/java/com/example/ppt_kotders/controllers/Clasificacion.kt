package com.example.ppt_kotders.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.ppt_kotders.R

class Clasificacion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clasificacion)

        val btSalir = findViewById<Button>(R.id.btSalir)



        btSalir.setOnClickListener(){
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }
}