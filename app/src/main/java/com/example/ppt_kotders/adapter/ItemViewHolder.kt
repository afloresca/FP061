package com.example.ppt_kotders.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ppt_kotders.models.JuegoModelo
import com.example.ppt_kotders.R

class ItemViewHolder(view:View):RecyclerView.ViewHolder(view) {

    val imagen = view.findViewById<ImageView>(R.id.imageView3)
    val resultado = view.findViewById<TextView>(R.id.textView2)
    val fechahora = view.findViewById<TextView>(R.id.textView5)
    fun render(game : JuegoModelo){

        resultado.text = game.resultado
        fechahora.text = game.fechahora
        when(game.resultado){
            "Victoria"->imagen.setImageResource(R.drawable.tijera)
            "Derrota"->imagen.setImageResource(R.drawable.piedra)
        }


    }

}