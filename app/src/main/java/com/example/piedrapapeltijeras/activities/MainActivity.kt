package com.example.piedrapapeltijeras.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.piedrapapeltijeras.R
import com.example.piedrapapeltijeras.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initGame()
        Log.d(TAG, "Iniciando juego")

    }

    fun initGame(){
        val button = findViewById<ImageButton>(R.id.ibStart)
        button.setOnClickListener{
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    }
