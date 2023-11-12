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
import com.example.ppt_kotders.ui.theme.PPT_KotdersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)

        val loginbt = findViewById<Button>(R.id.btlog)
        val inserttxt = findViewById<EditText>(R.id.editTextText2)
        val textview =findViewById<TextView>(R.id.textView8)

        val MyDBOpenHelper = MyDBOpenHelper(this,null)

        // Se introduce el texto

        loginbt.setOnClickListener(){

            var nombreplayer =inserttxt.text.toString()
            var id = MyDBOpenHelper.getUserID(nombreplayer)

            if(id==0){ // Si existe el 1er jug guarda el id y accede automatico
                MyDBOpenHelper.addPlayer(nombreplayer, 0)

                // Establecer filtro login
            }

            id = MyDBOpenHelper.getUserID(nombreplayer)
            val intent = Intent(this, Menu::class.java)
            intent.putExtra("Jugador_ID", id)
            startActivity(intent)

        }





        /*setContent {
             PPT_KotdersTheme {
                 // A surface container using the 'background' color from the theme
                 Surface(
                     modifier = Modifier.fillMaxSize(),
                     color = MaterialTheme.colorScheme.background
                 ) {
                     Greeting("Android")
                 }
             }
         */
        //}
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















