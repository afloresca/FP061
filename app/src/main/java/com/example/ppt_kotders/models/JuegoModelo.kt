package com.example.ppt_kotders.models

class JuegoModelo{

    var nombreJugador : String = ""
    var resultado : String = ""
    var fechahora: String = ""

    constructor(nombreJugador: String, resultado: String,fechahora:String){
        this.nombreJugador = nombreJugador
        this.resultado= resultado
        this.fechahora = fechahora
    }
}