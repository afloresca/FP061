package com.example.ppt_kotders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ppt_kotders.models.JuegoModelo
import com.example.ppt_kotders.R

class ItemAdapter(val list : List<JuegoModelo>): RecyclerView.Adapter<ItemViewHolder>(){
    // Recibe la lista que se creara en el historico
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutinflater.inflate(R.layout.activity_item, parent,false))
    }

    override fun getItemCount(): Int {
        print(list.size)
        return list.size}

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = list[position]
            holder.render(item)
    }
}