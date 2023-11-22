package com.example.ppt_kotders.services

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class CurrentLocation : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    init {

    }
    fun onCreate(savedInstanceState: Bundle?){
       fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}