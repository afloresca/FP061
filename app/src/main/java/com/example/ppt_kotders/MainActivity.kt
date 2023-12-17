package com.example.ppt_kotders

import MyDBOpenHelper
import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.example.ppt_kotders.controllers.Menu
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.util.Locale


class MainActivity : ComponentActivity() {

    private val CODIGO_PERMISO_SEGUNDO_PLANO = 100
    private var isPermisos = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val CODE_GOOGLE_SIGN_IN = 2
    private lateinit var mAuth: FirebaseAuth

    private lateinit var textview: TextView
    private lateinit var myDBOpenHelper: MyDBOpenHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val loginbt = findViewById<Button>(R.id.btlog)
        val inserttxt = findViewById<EditText>(R.id.editTextText2)
        val textview = findViewById<TextView>(R.id.textView8)
        val btcambio = findViewById<Button>(R.id.button2)
        val btcambio2 = findViewById<Button>(R.id.button1)
        val myDBOpenHelper = MyDBOpenHelper(this, null)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Se introduce el texto

        loginbt.setOnClickListener() {

            var nombreplayer = inserttxt.text.toString()

            if (nombreplayer == "") {
                textview.text = getString(R.string.not_valid)
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

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    fun callSignInGoogle (view: View){
        signInGoogle()
    }

    private fun signInGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        var googleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, CODE_GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CODE_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken:String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    val user = mAuth.currentUser
                    updateUI(user)

                    // Obtener el nombre de usuario de Google
                    val nombreUsuario = user?.displayName ?: ""
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun saveUsernameToLocalDB(nombreUsuario: String) {
        if (nombreUsuario.isNotEmpty()) {
            // Verificar si el usuario ya existe en la base de datos local
            var id = myDBOpenHelper.getUserID(nombreUsuario)

            if (id == 0) {
                // Si no existe, agregar el usuario a la base de datos local
                myDBOpenHelper.addPlayer(nombreUsuario, 0)
            }
            id = myDBOpenHelper.getUserID(nombreUsuario)
            val intent = Intent(this, Menu::class.java)

            // Establecer información en el Singleton o donde lo necesites
            UserSingelton.id = id
            startActivity(intent)

        }
    }

    private fun loginWithLocalPlayer(nombreplayer: String) {
        if (nombreplayer == "") {
            textview.text = getString(R.string.not_valid)
        } else {
            var id = myDBOpenHelper.getUserID(nombreplayer)

            if (id == 0) {
                // Si no existe el jugador, crea uno nuevo
                myDBOpenHelper.addPlayer(nombreplayer, 0)
            }

            id = myDBOpenHelper.getUserID(nombreplayer)
            val intent = Intent(this, Menu::class.java)

            // Establecer información en el Singleton o donde lo necesites
            UserSingelton.id = id
            startActivity(intent)
        }
    }

    private fun updateUI(user: FirebaseUser?) {

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















