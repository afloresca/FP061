package com.example.ppt_kotders.controllers

import android.Manifest
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.CalendarContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ppt_kotders.FirebaseApiService
import com.example.ppt_kotders.MainActivity
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton
import com.example.ppt_kotders.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.TimeZone

class SolucionJuego : AppCompatActivity() {

    var lista = arrayOfNulls<MediaPlayer>(size = 2)

    private val CALENDAR_WRITE_PERMISSION_REQUEST_CODE = 101
    private val CALENDAR_READ_PERMISSION_REQUEST_CODE = 102

    private val myDBFirebase = FirebaseDatabase
        .getInstance("https://kotders-dbenitez-default-rtdb.firebaseio.com/")
        .reference
    var puntospremio = 0
    val endpointUrl =
        "https://kotders-dbenitez-default-rtdb.firebaseio.com/premios/1/puntos"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solucion)

        lista[0]=MediaPlayer.create(this,R.raw.fireworks)
        lista[1]=MediaPlayer.create(this,R.raw.trueno)


        val idUser = UserSingelton.id
        val condicion = UserSingelton.estado
        val buttonD = findViewById<Button>(R.id.buttonD)
        val buttonSave = findViewById<Button>(R.id.buttonSaveGame)
        val imagen = findViewById<ImageView>(R.id.imageView)
        val texto = findViewById<TextView>(R.id.textView3)
        val textodata = findViewById<TextView>(R.id.textView)
        textodata.text = idUser.toString()




        if (idUser == -1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fun obtenerPuntos(endpointUrl: String):Int {
            var puntos = 0
            try {
                val url = URL(endpointUrl)
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"

                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line)
                    }

                    puntos = stringBuilder.toString().toInt()
                } else {
                    // Manejar error en la respuesta
                    Log.e("ObtenerPuntos", "Error en la respuesta: $responseCode")
                }

            } catch (e: Exception) {
                // Manejar error en la solicitud
                Log.e("ObtenerPuntos", "Error en la solicitud: ${e.message}")
            }
            return puntos
        }

        puntospremio = obtenerPuntos(endpointUrl)

        fun sobrescribirPuntos(endpointUrl: String, nuevosPuntos: Int) {
            val url = URL(endpointUrl)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "PUT"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val outputStream = OutputStreamWriter(connection.outputStream)
            outputStream.write(nuevosPuntos.toString())
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode

            if (responseCode != HttpURLConnection.HTTP_OK) {
                // Manejar error en la respuesta
                throw RuntimeException("Error en la solicitud PUT. Código de respuesta: $responseCode")
            }
        }

        fun updateDataRealtimeDB(email: String, nombre: String, nuevosPuntos: Int, nuevasVictorias: Int) {
            val userUID = UserSingelton.getUID()
            val dbReference = myDBFirebase.child("usuarios").child(userUID)

            // Crear un mapa para actualizar solo puntos y victorias
            val userUpdate = HashMap<String, Any>()
            userUpdate["email"] = email
            userUpdate["nombre"] = nombre
            userUpdate["puntos"] = nuevosPuntos
            userUpdate["victorias"] = nuevasVictorias

            // Actualizar la base de datos con el nuevo mapa
            dbReference.updateChildren(userUpdate)
                .addOnSuccessListener {
                    Log.d("updateDataRealtimeDB", "Datos actualizados exitosamente.")
                }
                .addOnFailureListener {
                    Log.e("updateDataRealtimeDB", "Error al actualizar datos.", it)
                }
        }

        fun changeUserDataRealtimeDB(user: User?) {
            if (user != null) {
                val premio =puntospremio
                val nombre = user.nombre
                val email = user.email
                val puntosActuales = user.puntos ?: 0
                val victoriasActuales = user.victorias ?: 0
                val nuevosPuntos = puntosActuales + 1 + premio
                val nuevasVictorias = victoriasActuales + 1

                // Actualizar solo los campos de puntos y victorias en la base de datos
                updateDataRealtimeDB(email!!, nombre!!, nuevosPuntos, nuevasVictorias)
            }
        }

        fun readUserDataFromRealtimeDB() {
            val userUID = UserSingelton.getUID()
            val dbReference = myDBFirebase.child("usuarios")

            // Buscar al usuario por su UID en la base de datos en tiempo real
            dbReference.child(userUID).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("onCancelled", "Error!", error.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // El usuario existe en la base de datos
                        val user = snapshot.getValue(User::class.java)

                        // Lógica para procesar los datos leídos (si es necesario)
                        changeUserDataRealtimeDB(user)
                    } else {
                        Log.e("onDataChange", "Usuario no encontrado en la base de datos.")
                        // Puedes manejar el caso en que el usuario no se encuentre en la base de datos.
                    }
                }
            })
        }

        when (condicion) {

            1 -> {
                imagen.setImageResource(R.drawable.ppt)
                texto.text = getString(R.string.tx_victory)
                lista[0]?.start()
                buttonSave.visibility = android.view.View.VISIBLE
                readUserDataFromRealtimeDB()
                sobrescribirPuntos(endpointUrl,0)
            }

            2 -> {
                imagen.setImageResource(R.drawable.historico_1_photoroom_png_photoroom)
                texto.text = getString(R.string.tx_loose)
                lista[1]?.start()
                val prize = puntospremio +1
                sobrescribirPuntos(endpointUrl,prize)
            }

            0 -> logout() // Por defecto -> error del juego
        }

        buttonD.setOnClickListener() {

            val intent = Intent(this, Menu::class.java)
            UserSingelton.estado = 0 // Lo ponemos por defecto
            lista[0]?.stop()
            lista[1]?.stop()
            startActivity(intent)
            finish()

        }

        // Función para guardar la captura en la galeria de imagenes
        fun saveMediaToStorage(bitmap: Bitmap) {
            val nombrearchivo = "${System.currentTimeMillis()}.jpg"
            var fos: OutputStream? = null


            // Determinar la ruta de almacenamiento
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                this.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, nombrearchivo)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }
                    val imageUri: android.net.Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    fos = imageUri?.let { resolver.openOutputStream(it) }
                }
            } else {
                val imagesDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, nombrearchivo)
                fos = FileOutputStream(image)
            }

            fos?.use {
                // Comprimir y guardar la captura en la galeria
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(
                    this@SolucionJuego,
                    "Captura realizada y guardada en la galería",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun takeScreenshot() {
            // Obtener la vista raíz de la actividad
            val rootView = window.decorView.rootView

            // Tomar captura de pantalla
            val bitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            rootView.draw(canvas)

            // Guardar captura de pantalla en la galería
            saveMediaToStorage(bitmap)
        }

        fun insertEventToCalendar() {
            // Verificar permiso de escritura en calendario
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_CALENDAR
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_CALENDAR), CALENDAR_WRITE_PERMISSION_REQUEST_CODE
                )
                return
            }

            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_CALENDAR
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_CALENDAR), CALENDAR_READ_PERMISSION_REQUEST_CODE
                )
                return
            }

            // Obtener el ID del calendario predeterminado de Google
            val projection = arrayOf(
                CalendarContract.Calendars._ID, CalendarContract.Calendars.ACCOUNT_TYPE
            )
            val selection =
                "${CalendarContract.Calendars.IS_PRIMARY} = 1 AND ${CalendarContract.Calendars.ACCOUNT_TYPE} = ?"
            val selectionArgs = arrayOf("com.google")
            val cursor = this.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI, projection, selection, selectionArgs, null
            )

            var calendarId = -1
            if (cursor != null && cursor.moveToFirst()) {
                calendarId = cursor.getInt(0)
                cursor.close()
            }

            // Guardar evento en el calendario predeterminado
            val beginTime: Long = System.currentTimeMillis()
            val endTime: Long = beginTime + 3600000
            val timeZone: TimeZone = TimeZone.getDefault()

            val values = ContentValues().apply {
                put(CalendarContract.Events.TITLE, "Victoria en Piedra, Papel o Tijeras by Kotders")
                put(CalendarContract.Events.DESCRIPTION, "¡Enorabuena, has ganado!")
                put(CalendarContract.Events.DTSTART, beginTime)
                put(CalendarContract.Events.DTEND, endTime)
                put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.id)
                put(CalendarContract.Events.CALENDAR_ID, calendarId)
                put(CalendarContract.Events.HAS_ALARM, 1)
            }

            // Insertar evento en el calendario
            val uri =
                this.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

            if (uri != null) {
                // Verificar que el evento se ha guardado en el calendario predeterminado
                val eventProjection = arrayOf(CalendarContract.Events.CALENDAR_ID)
                val eventCursor = this.contentResolver.query(
                    uri, eventProjection, null, null, null
                )

                if (eventCursor != null && eventCursor.moveToFirst()) {
                    val savedCalendarId = eventCursor.getInt(0)
                    if (savedCalendarId == calendarId) {
                        println("Evento guardado en el calendario predeterminado")
                        println(uri.toString())
                        Toast.makeText(this, "Evento guardado en el calendario", Toast.LENGTH_SHORT).show()
                    } else {
                        println("Error: el evento no se ha guardado en el calendario predeterminado")
                        Toast.makeText(this, "Error: el evento no se ha guardado en el calendario", Toast.LENGTH_SHORT).show()
                    }
                    eventCursor.close()
                }
            } else {
                println("Error guardando el evento en el calendario")
                Toast.makeText(this, "Error guardando el evento en el calendario", Toast.LENGTH_SHORT).show()
            }

        }


        buttonSave.setOnClickListener {
            takeScreenshot()
            insertEventToCalendar()
        }
    }

    fun logout(){ // Detecta el estado default y rectifica el error logout
        val intent = Intent(this, MainActivity::class.java)
        UserSingelton.id = 0
        UserSingelton.estado = 0
        startActivity(intent)
        finish()

    }

}




