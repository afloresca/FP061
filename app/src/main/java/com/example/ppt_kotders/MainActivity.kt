package com.example.ppt_kotders

import MyDBOpenHelper
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ppt_kotders.controllers.Menu
import com.example.ppt_kotders.ui.theme.PPT_KotdersTheme
import java.util.Locale


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        PPT_KotdersTheme {
            Greeting("Android")
        }
    }

















