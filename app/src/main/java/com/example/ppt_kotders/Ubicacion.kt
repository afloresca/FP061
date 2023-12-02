package com.example.ppt_kotders

import androidx.compose.runtime.Composable
import java.nio.DoubleBuffer

class Ubicacion {

    companion object{
        @JvmStatic
        private var longitud : Double = 0.0
        @JvmStatic
        private var latitud : Double = 0.0

        fun setLontigud(lon : Double){
            longitud = lon
        }
        fun setLatitud(lat : Double){
            latitud = lat
        }

        fun getLongitud() : Double{
            return longitud
        }
        fun getLatitud() : Double {
            return latitud
        }

    }
}