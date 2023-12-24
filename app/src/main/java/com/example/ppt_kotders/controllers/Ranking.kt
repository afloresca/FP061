package com.example.ppt_kotders.controllers


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ppt_kotders.R
import com.example.ppt_kotders.adapter.PlayerAdapter
import com.example.ppt_kotders.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Ranking : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clasificacion)
        initRecycleView2()

        val btSalir = findViewById<Button>(R.id.buttonSal)




        btSalir.setOnClickListener() {
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initRecycleView2() {
        try {
            if (!isFinishing()){
            val recyclerView = findViewById<RecyclerView>(R.id.recyclePlayer)
            getAllUsersFromRealtimeDB { topPlayersList ->
                // Configura el RecyclerView con el adaptador y la lista de los mejores jugadores
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = PlayerAdapter(topPlayersList)
            }
            }
        } catch (e: Exception) {
            Log.e("initRecycleView2", "Error al inicializar RecyclerView: ${e.message}", e)
        }
    }

    private fun getAllUsersFromRealtimeDB(callback: (List<User>) -> Unit) {
        val myDBFirebase = FirebaseDatabase
            .getInstance("https://kotders-dbenitez-default-rtdb.firebaseio.com/")
            .reference
        val dbReference = myDBFirebase.child("usuarios")

        // Escuchar cambios en la base de datos
        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                // Manejar el evento de cancelación (error en la lectura de datos)
                Log.e("onCancelled", "Error!", error.toException())
                callback(emptyList()) // Devolver una lista vacía en caso de error
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val userList = mutableListOf<User>()
                    // Recorrer los nodos de usuarios y convertirlos a objetos User
                    for (userSnapshot in snapshot.children) {
                        val email = userSnapshot.child("email").getValue(String::class.java)
                        val nombre = userSnapshot.child("nombre").getValue(String::class.java)
                        val puntos = userSnapshot.child("puntos").getValue(Int::class.java) ?: 0
                        val victorias = userSnapshot.child("victorias").getValue(Int::class.java) ?: 0

                        val user = User( email.orEmpty(), nombre.orEmpty(), puntos, victorias)
                        userList.add(user)
                    }

                val listamejores = userList.sortedByDescending { it.victorias }
                val topPlayers = listamejores.take(10)

                callback(topPlayers)
            }
        })
    }


}