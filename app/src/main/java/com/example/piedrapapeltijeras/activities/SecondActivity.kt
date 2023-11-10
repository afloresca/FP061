package com.example.piedrapapeltijeras.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.piedrapapeltijeras.R
import com.example.piedrapapeltijeras.database.MyDBOpenHelper

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //Se establece la referencia con el botón de introducir el nombre
        val buttonName: Button = findViewById(R.id.btName)
        //Pulsación sobre el botón
        buttonName.setOnClickListener{ firstRegister()}
    }

    fun firstRegister(){
        // Se establece la referencia con la casilla de EditText
        val editTextPlayerName: EditText = findViewById(R.id.etYourName)
        //
        val playerName = editTextPlayerName.text.toString()
        //
        val dbHandler = MyDBOpenHelper(this, null)
        //
        dbHandler.addPlayer(playerName, "0")

        //
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }


}