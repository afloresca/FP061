package com.example.ppt_kotders.models

class JuegoModelo{

    var nombreJugador : String = ""
    var resultado : String = ""
    var fechahora: String = ""
    var latitud : Double = 0.0
    var longitud : Double = 0.0

    constructor(nombreJugador: String, resultado: String,fechahora:String, latitud: Double, longitud : Double){
        this.nombreJugador = nombreJugador
        this.resultado= resultado
        this.fechahora = fechahora
        this.longitud = longitud
        this.latitud = latitud
    }

    constructor(nombreJugador: String, resultado: String,fechahora:String){
        this.nombreJugador = nombreJugador
        this.resultado= resultado
        this.fechahora = fechahora
    }
}