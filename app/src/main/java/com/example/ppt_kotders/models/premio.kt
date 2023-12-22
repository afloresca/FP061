package com.example.ppt_kotders.models

class premio {
    constructor(id: Int, puntos: Int) {
        this.id = id
        this.puntos = puntos
    }

    private val id: Int
    private val puntos: Int


    override fun toString(): String {
        return "premio(id=$id, puntos=$puntos)"
    }

}