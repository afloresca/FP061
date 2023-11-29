package com.example.ppt_kotders.controllers

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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


class SolucionJuego : AppCompatActivity() {

    private val CALENDAR_REQUEST_CODE = 123

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

        buttonScreenshot.setOnClickListener {
            // Verificar permisos de calendario
            checkCalendarPermission()
        }
    }

    fun saveVictoryInCalendar() {

        val calendarId = 1 
        val startTimeMillis = System.currentTimeMillis()
        val endTimeMillis = startTimeMillis + 3600000

        try {
            val values = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startTimeMillis)
                put(CalendarContract.Events.DTEND, endTimeMillis)
                put(CalendarContract.Events.TITLE, "Victoria en Piedra, Papel o Tijeras by Kotders")
                put(CalendarContract.Events.CALENDAR_ID, calendarId)
                put(CalendarContract.Events.DESCRIPTION, "¡Enhorabuena, has ganado!")
            }
            val uri: Uri? = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

            // Comprueba si la inserción fue exitosa
            if (uri == null) {
                Log.e("SolucionJuego", "Error al insertar evento en el calendario.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("SolucionJuego", "Excepción al insertar evento en el calendario: ${e.message}")
        }

        runOnUiThread {
            Toast.makeText(
                this@SolucionJuego,
                "Evento añadido al calendario",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun takeScreenshotAndSave() {
        // Tomar captura de pantalla
        val bitmap = takeScreenshot(this@SolucionJuego)

        // Guardar captura de pantalla en la galería
        if (bitmap != null) {
            saveMediaToStorage(bitmap)
        }

        // Guardar victoria en el calendario
        saveVictoryInCalendar()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // Llama a la implementación de la clase base

        when (requestCode) {
            CALENDAR_REQUEST_CODE -> {
                // Verifica si ambos permisos fueron concedidos
                val writeCalendarPermissionGranted =
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readCalendarPermissionGranted =
                    grantResults.size > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (writeCalendarPermissionGranted && readCalendarPermissionGranted) {
                    // Ambos permisos concedidos, ahora puedes realizar operaciones relacionadas con el calendario
                    takeScreenshotAndSave()
                } else {
                    // Al menos uno de los permisos fue denegado, puedes informar al usuario o realizar otra acción
                    Toast.makeText(
                        this@SolucionJuego,
                        "Permisos de calendario denegados",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else -> {
                // Otros casos
            }
        }
    }

    private fun checkCalendarPermission() {
        val writeCalendarPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_CALENDAR
        )
        val readCalendarPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CALENDAR
        )

        if (writeCalendarPermission != PackageManager.PERMISSION_GRANTED ||
            readCalendarPermission != PackageManager.PERMISSION_GRANTED
        ) {
            // Los permisos no están aceptados.
            requestCalendarPermission()
        } else {
            // Los permisos están aceptados.
            takeScreenshotAndSave()
        }
    }

    private fun requestCalendarPermission() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_CALENDAR
        )

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1])
        ) {
            // El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
        } else {
            // El usuario nunca ha aceptado ni rechazado, así que le pedimos que acepte los permisos.
            ActivityCompat.requestPermissions(
                this,
                permissions,
                CALENDAR_REQUEST_CODE
            )
        }
    }

    private fun takeScreenshot(activity: Activity): Bitmap {
        val v1 = activity.window.decorView.rootView
        v1.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(v1.drawingCache)
        v1.isDrawingCacheEnabled = false

        return bitmap
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
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
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(
                this@SolucionJuego,
                "Captura realizada y guardada en la Galería",
                Toast.LENGTH_SHORT
            ).show()
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


