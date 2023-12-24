package com.example.ppt_kotders.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ppt_kotders.R
import com.example.ppt_kotders.models.JuegoModelo
import com.example.ppt_kotders.models.User

class PlayerAdapter(val list : List<User>): RecyclerView.Adapter<PlayerViewHolder>(){
        // Recibe la lista que se creara en el historico
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
            val layoutinflater = LayoutInflater.from(parent.context)
            return PlayerViewHolder(layoutinflater.inflate(R.layout.item_player, parent,false))
        }


    override fun getItemCount(): Int {
            print(list.size)
            return list.size}

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
            val item = list[position]
            holder.render(item)
        }




}
