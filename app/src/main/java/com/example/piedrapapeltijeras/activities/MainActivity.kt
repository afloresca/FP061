package com.example.piedrapapeltijeras.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.piedrapapeltijeras.R
import com.example.piedrapapeltijeras.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initGame()


        // Constantes del bottom navigation
        val optionsFragment = OptionsFragment()
        val languageFragment = LanguageFragment()
        val googlePlayFragment = GooglePlayFragment()

        /**
         *

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_options -> {
                    setCurrentFragment(optionsFragment)
                    true
                }

                R.id.nav_language -> {
                    setCurrentFragment(languageFragment)
                    true
                }

                R.id.nav_googleplay -> {
                    setCurrentFragment(googlePlayFragment)
                    true
                }

                else -> false
            }
        }
        */
    }

    fun initGame(){
        val button = findViewById<ImageButton>(R.id.ibStart)
        button.setOnClickListener{
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    }
