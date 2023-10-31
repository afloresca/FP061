package com.example.piedrapapeltijeras.models

class Player (
    var playerId: Int,
    var playerName: String,
    var coins: Int = 0
){
    // Constructor
    init {
        this.playerId = playerId
        this.playerName = playerName
        this.coins = coins
    }
}