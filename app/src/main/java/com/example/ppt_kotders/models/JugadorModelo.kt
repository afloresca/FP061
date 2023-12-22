package com.example.ppt_kotders.models

class JugadorModelo {

     var id : Int
     var nombre : String
     var email: String
     var puntuacion : Int

    constructor(id: Int, nombre: String, email: String, puntuacion: Int) {
        this.id = id
        this.nombre = nombre
        this.email = email
        this.puntuacion = puntuacion
    }
}

