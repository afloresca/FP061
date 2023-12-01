package com.example.ppt_kotders

import java.util.Locale

object UserSingelton {
    var id : Int = 0
    var estado = 0
    var lang = Locale.getDefault().language
}