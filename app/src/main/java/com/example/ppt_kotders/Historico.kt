package com.example.ppt_kotders

import MyDBOpenHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ppt_kotders.adapter.ItemAdapter

class Historico : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)
        crearpartidas()
        initREcycleView()

        val btSalir = findViewById<Button>(R.id.btSalir)



        btSalir.setOnClickListener(){
            val intent = Intent(this,Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initREcycleView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recycleItem)
        val myDBOpenHelper = MyDBOpenHelper(this,null)
        val idUser=UserSingelton.id
        val list = myDBOpenHelper.listPlayerGames(idUser)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ItemAdapter(list)
    }

    private fun crearpartidas(){
        val myDBOpenHelper = MyDBOpenHelper(this,null)
        val idUser=UserSingelton.id
        val jugador=myDBOpenHelper.getUser(idUser)
        myDBOpenHelper.addGame(jugador.nombre,"Victoria")



    }
}