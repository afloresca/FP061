package com.example.ppt_kotders.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ppt_kotders.R
import com.example.ppt_kotders.models.User

class PlayerViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val imagen = view.findViewById<ImageView>(R.id.imageUser)
    val nombre = view.findViewById<TextView>(R.id.puntosUser)
    val puntos = view.findViewById<TextView>(R.id.nombreUser)

    fun render(user : User){

        imagen.setImageResource(R.drawable.piedra)
        nombre.text = user.nombre ?:"undefined"
        puntos.text = (user.victorias?.toString() ?: "0")


    }


}
