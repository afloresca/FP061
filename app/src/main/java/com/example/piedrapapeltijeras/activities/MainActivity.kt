package com.example.piedrapapeltijeras.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.piedrapapeltijeras.R
import com.example.piedrapapeltijeras.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val optionsFragment = OptionsFragment()
        val languageFragment = LanguageFragment()
        val googlePlayFragment = GooglePlayFragment()

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
    }

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.ContainerView, fragment)
            commit()
        }
    }
}