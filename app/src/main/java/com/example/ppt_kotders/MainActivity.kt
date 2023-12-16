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
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val CODIGO_PERMISO_SEGUNDO_PLANO = 100
    private var isPermisos = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val REQ_ONE_TAP = 2
    private var showOneTapUI = true
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        verificarPermisos()

        val loginbt = findViewById<Button>(R.id.btlog)
        val inserttxt = findViewById<EditText>(R.id.editTextText2)
        val textview = findViewById<TextView>(R.id.textView8)
        val btcambio = findViewById<Button>(R.id.button2)
        val btcambio2 = findViewById<Button>(R.id.button1)
        val myDBOpenHelper = MyDBOpenHelper(this, null)

        // Initialize Firebase Auth
        auth = Firebase.auth

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
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun callSignInGoogle (view: View){
        signInGoogle()
    }

    private fun signInGoogle(){
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = googleCredential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with Firebase.
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success")
                                        val user = auth.currentUser
                                        updateUI(user)
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                                        updateUI(null)
                                    }
                                }
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    // ...
                }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    private fun verificarPermisos() {
        val permisos = arrayListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            permisos.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        val permisosArray = permisos.toTypedArray()
        if (tienePermisos(permisosArray)){
            isPermisos = true
            onPermisosConcedidos()
        } else {
            solicitarPermisos(permisosArray)
        }

    }

    private fun tienePermisos(permisos: Array<String>): Boolean{
        return permisos.all{
            return ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun onPermisosConcedidos() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        try{
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null){
                    obtenerUbicacion(it)
                } else {
                    Toast.makeText(this, "No se puede obtener la ubicación", Toast.LENGTH_SHORT).show()
                }

            }
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                30000
            ).apply {
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                setWaitForAccurateLocation(true)
            }.build()

            locationCallback = object  :  LocationCallback() {
                override fun onLocationResult(p0 : LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations) {
                        obtenerUbicacion(location)
                    }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (_: SecurityException){

        }
    }

    private fun solicitarPermisos(permisos: Array<String>){
        requestPermissions(
            permisos,
            CODIGO_PERMISO_SEGUNDO_PLANO
        )
    }

    private fun obtenerUbicacion(ubicacion: Location){
        Ubicacion.setLatitud(ubicacion.latitude)
        Ubicacion.setLontigud(ubicacion.longitude)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CODIGO_PERMISO_SEGUNDO_PLANO){
            val todosPermisosConcedidos = grantResults.all { it == PackageManager.PERMISSION_GRANTED  }
            if (grantResults.isNotEmpty() && todosPermisosConcedidos) {
                isPermisos = true
                onPermisosConcedidos()
            }
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















