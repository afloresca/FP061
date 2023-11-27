package com.example.ppt_kotders.controllers

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ppt_kotders.R

class ayuda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda)

        val salirbt = findViewById<Button>(R.id.btSalir)
        webViewSetup()


        salirbt.setOnClickListener{
            val intent = Intent(this,Menu::class.java)
            startActivity(intent)
            finish()

        }

    }



    private fun webViewSetup(){
        val kwebview = findViewById<WebView>(R.id.kwebview)
        kwebview.webViewClient = WebViewClient()

        kwebview.apply {
            loadUrl("https://sites.google.com/view/kotders-ayuda/inicio")
            settings.javaScriptEnabled = true

        }

    }

}