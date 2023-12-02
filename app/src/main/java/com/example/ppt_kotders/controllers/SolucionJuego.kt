package com.example.ppt_kotders.controllers

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
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
import com.example.ppt_kotders.MainActivity
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import android.net.Uri
import android.view.View
import java.util.TimeZone


class SolucionJuego : AppCompatActivity() {

    private val CALENDAR_WRITE_PERMISSION_REQUEST_CODE = 101
    private val CALENDAR_READ_PERMISSION_REQUEST_CODE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solucion)

        val idUser = UserSingelton.id
        val condicion = UserSingelton.estado
        val buttonD = findViewById<Button>(R.id.buttonD)
        val buttonScreenshot = findViewById<Button>(R.id.buttonScreenshot)
        val imagen = findViewById<ImageView>(R.id.imageView)
        val texto = findViewById<TextView>(R.id.textView3)
        val textodata = findViewById<TextView>(R.id.textView)
        textodata.text = idUser.toString()

        if (idUser == -1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        when (condicion) {
            1 -> {
                imagen.setImageResource(R.drawable.ppt)
                texto.text = " VICTORIA "
                buttonScreenshot.visibility = android.view.View.VISIBLE
            }

            2 -> {
                imagen.setImageResource(R.drawable.historico_1_photoroom_png_photoroom)
                texto.text = " DERROTA "
            }

            0 -> logout() // Por defecto -> error del juego
        }

        buttonD.setOnClickListener {
            val intent = Intent(this, Menu::class.java)
            UserSingelton.estado = 0 // Lo ponemos por defecto
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


        buttonScreenshot.setOnClickListener {
            takeScreenshot()
            insertEventToCalendar()
        }

    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        UserSingelton.id = 0
        UserSingelton.estado = 0
        startActivity(intent)
        finish()
    }
}


