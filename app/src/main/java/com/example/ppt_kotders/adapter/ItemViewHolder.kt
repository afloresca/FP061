package com.example.ppt_kotders.adapter

import android.content.res.Resources
import android.provider.Settings.Global.getString
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.res.stringResource
import androidx.recyclerview.widget.RecyclerView
import com.example.ppt_kotders.models.JuegoModelo
import com.example.ppt_kotders.R



class ItemViewHolder(view:View):RecyclerView.ViewHolder(view) {

    val imagen = view.findViewById<ImageView>(R.id.imageView3)
    val resultado = view.findViewById<TextView>(R.id.textView2)
    val fechahora = view.findViewById<TextView>(R.id.textView5)

    fun render(game : JuegoModelo){

        fechahora.text = game.fechahora
        when(game.resultado){
            "Victoria"->{
                imagen.setImageResource(R.drawable.tijera)
                resultado.apply { resultado.text=context.resources.getString(R.string.tx_victory) }
            }
            "Derrota"-> {
                imagen.setImageResource(R.drawable.piedra)
                resultado.apply { resultado.text=context.resources.getString(R.string.tx_loose) }

            }
            }
        }


    }



