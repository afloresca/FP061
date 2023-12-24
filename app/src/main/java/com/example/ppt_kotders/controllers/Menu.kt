package com.example.ppt_kotders.controllers

import MyDBOpenHelper
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.BounceInterpolator
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ppt_kotders.MainActivity
import com.example.ppt_kotders.R
import com.example.ppt_kotders.UserSingelton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class Menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu) // layout menu

        val jugarBt = findViewById<Button>(R.id.btjugar)
        val historicoBt = findViewById<Button>(R.id.bthistorico)
        val clasificacionBt = findViewById<Button>(R.id.btclasificacion)
        val ayudaBt = findViewById<Button>(R.id.btayuda)
        val nombre = findViewById<TextView>(R.id.textNombre)
        val puntos = findViewById<TextView>(R.id.textPuntos)

        val MyDBOpenHelper = MyDBOpenHelper(this, null)
        var idUser = UserSingelton.id
        val origen = jugarBt.y


        MyDBOpenHelper.getUser(idUser)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ jugador ->
                // Actualizar la interfaz de usuario con los datos del jugador
                nombre.text = jugador.nombre
                puntos.text = jugador.puntuacion.toString()
            }, { error ->
                // Manejar el error, si es necesario
                TODO()
            })


        if (idUser == -1) { // LogOut de seguridad
            val intent = Intent(this, MainActivity::class.java)
            UserSingelton.id = 0
            startActivity(intent)
            finish()

        }
        fun animateButton(button: Button) {

            val finalY = button.y
            button.y = -button.height.toFloat()
            val animator = ObjectAnimator.ofFloat(button, "y", finalY)
            animator.duration = 1000
            animator.interpolator = BounceInterpolator()
            animator.start()
        }

        jugarBt.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                jugarBt.viewTreeObserver.removeOnPreDrawListener(this)
                Handler(Looper.getMainLooper()).postDelayed({

                    animateButton(jugarBt)
                }, 200)
                return true
            }
        })

        historicoBt.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                jugarBt.viewTreeObserver.removeOnPreDrawListener(this)

                animateButton(historicoBt)
                return true
            }
        })

        clasificacionBt.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                jugarBt.viewTreeObserver.removeOnPreDrawListener(this)

                animateButton(clasificacionBt)
                return true
            }
        })


        jugarBt.setOnClickListener {

            val intent = Intent(this, Juego::class.java)
            startActivity(intent)
            finish()

        }

        ayudaBt.setOnClickListener{
            val intent = Intent(this, ayuda::class.java)
            startActivity(intent)
            finish()
        }

        historicoBt.setOnClickListener {
            val intent = Intent(this, Historico::class.java)
            startActivity(intent)
            finish()

        }

        clasificacionBt.setOnClickListener {
            val intent = Intent(this, Clasificacion::class.java)
            startActivity(intent)
            finish()

        }
    }
}