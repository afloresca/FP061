package com.example.ppt_kotders

import MyDBOpenHelper
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.ppt_kotders.controllers.Menu
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.mlkit.vision.text.Text
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val CODIGO_PERMISO_SEGUNDO_PLANO = 100
    private var isPermisos = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //verificarPermisos()

        val loginbt = findViewById<Button>(R.id.btlog)
        val inserttxt = findViewById<EditText>(R.id.editTextText2)
        val textview = findViewById<TextView>(R.id.textView8)
        val btcambio = findViewById<Button>(R.id.buttonLang)
        val btcambio2 = findViewById<Button>(R.id.buttonLang2)
        val myDBOpenHelper = MyDBOpenHelper(this, null)


        // Se introduce el texto

        loginbt.setOnClickListener() {

            var nombreplayer = inserttxt.text.toString()

            if (nombreplayer == "") {
                textview.text = getString(R.string.not_valid)
                //textview.text = UserSingelton.lang.toString()
            } else {
                var id = myDBOpenHelper.getUserID(nombreplayer)

                if (id == 0) { // Si existe el 1er jug guarda el id y accede automatico
                    myDBOpenHelper.addPlayer(nombreplayer, 0)
                    // Establecer filtro login
                }

                id = myDBOpenHelper.getUserID(nombreplayer)
                val intent = Intent(this, Menu::class.java)

                UserSingelton.id = id
                startActivity(intent)
            }


        }

            btcambio.setOnClickListener() {

            cambiolang("en_US")
            val refresh = Intent(this, MainActivity::class.java)
            startActivity(refresh)
            finish()


            }

            btcambio2.setOnClickListener(){
                cambiolang("es")
                val refresh = Intent(this, MainActivity::class.java)
                startActivity(refresh)
                finish()
            }

        }

        private fun cambiolang(lang:String){
            val myLocale = Locale(lang)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)

        }

    }





















