package com.example.ppt_kotders

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Historico : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)

        val btSalir = findViewById<Button>(R.id.btSalir)

        btSalir.setOnClickListener(){
            val intent = Intent(this,Menu::class.java)
            startActivity(intent)
            finish()
        }
    }
}