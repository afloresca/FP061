package com.example.piedrapapeltijeras.models

import com.example.piedrapapeltijeras.enums.Gesture
import com.example.piedrapapeltijeras.enums.Result
import java.util.Date

class Game(
    var gameId: Int,
    var playerId: Int,
    var opponentGesture: Gesture,
    var playerGesture: Gesture,
    var result: Result,
    var coinsChange: Int = 10,
    var timeStamp: Date
) {

}