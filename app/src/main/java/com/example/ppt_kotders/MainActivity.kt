package com.example.ppt_kotders

import MyDBOpenHelper
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.ppt_kotders.controllers.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btcambio = findViewById<Button>(R.id.button2)
        val btcambio2 = findViewById<Button>(R.id.button1)
        val myDBOpenHelper = MyDBOpenHelper(this, null)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        btcambio.setOnClickListener() {

            cambiolang("en_US")
            val refresh = Intent(this, MainActivity::class.java)
            startActivity(refresh)
            finish()
        }

        btcambio2.setOnClickListener() {
            cambiolang("es")
            val refresh = Intent(this, MainActivity::class.java)
            startActivity(refresh)
            finish()
        }
    }


    override fun onStart() {
        super.onStart()
        // Cerrar sesión al inicio de la aplicación
        mAuth.signOut()

        // Pide al usuario que inicie sesión
        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            // El usuario no ha iniciado sesión, realiza las acciones necesarias (por ejemplo, muestra la pantalla de inicio de sesión)
            // ...

        } else {
            // El usuario ya ha iniciado sesión, puedes realizar acciones adicionales si es necesario
            updateUI(currentUser)
        }
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
                    user?.let {
                        // Guardar la información del usuario en SavedPreferencesUser
                        UserSingelton.setEmail(this, it.email ?: "")
                        UserSingelton.setUsername(this, it.displayName ?: "")
                    }
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


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val userName = user.displayName
            Toast.makeText(this, "¡Hola, $userName!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Opcional: cierra la actividad actual si ya no es necesaria
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















