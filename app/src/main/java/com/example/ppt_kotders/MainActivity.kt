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
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.example.ppt_kotders.controllers.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.example.ppt_kotders.models.User
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale


class MainActivity : ComponentActivity() {

    private val CODIGO_PERMISO_SEGUNDO_PLANO = 100
    private var isPermisos = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val CODE_GOOGLE_SIGN_IN = 2
    private lateinit var mAuth: FirebaseAuth

    private val myDBFirebase = FirebaseDatabase
        .getInstance("https://kotders-dbenitez-default-rtdb.firebaseio.com/")
        .reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        verificarPermisos()

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

                    // Actualizar la información del usuario en Realtime Database
                    updateUserDataInRealtimeDB(user)

                    user?.let {
                        // Guardar la información del usuario en SavedPreferencesUser
                        UserSingelton.setEmail(this, it.email ?: "")
                        UserSingelton.setUsername(this, it.displayName ?: "")

                        // Guardar el UID del usuario en el singleton
                        UserSingelton.setUID(it.uid ?: "")
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

    private fun updateUserDataInRealtimeDB(user: FirebaseUser?) {
        user?.let {
            val newUser = User(it.email ?: "", it.displayName ?: "", 0, 0)

            // Obtener la referencia a la lista de usuarios en Firebase
            val usersReference = myDBFirebase.child("usuarios")

            // Agregar el nuevo usuario a la base de datos
            usersReference.child(it.uid ?: "").setValue(newUser)
                .addOnSuccessListener {
                    Log.d(TAG, "Usuario registrado en Firebase Realtime Database")
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error al registrar el usuario en Firebase Realtime Database", it)
                }
        }
    }


    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
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















