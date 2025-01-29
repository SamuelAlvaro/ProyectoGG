package com.example.guccigame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.animation.AnimationUtils
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        //Asociamos a la variable la imagen que tenemos:
        val logo = findViewById<ImageView>(R.id.logo)

        // Aplicamos el efecto
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        //Iniciamos el efecto
        logo.startAnimation(fadeIn)
        //Implementamos el handler que espera 3 segundos para cargar el activityMain
        Handler(Looper.getMainLooper()).postDelayed({
            logo.startAnimation(fadeIn)
            //Acabamos la actividad

        }, 4000)
        //ABRIMOS EL FICHERO SHARED PREFENCES
        val sharedPref = getSharedPreferences("MisPreferencias", MODE_PRIVATE)

        val usuario = sharedPref.getString("Usuario", "")
        val passwd = sharedPref.getString("Passwd", "")

        if (usuario == "" && passwd == ""){
                //CARGAR ACTIVIDAD DE SELECCIÓN DE LOGIN Y REGISTRO


            //Iniciamos la actividad principal
            startActivity(Intent(this, LoginRegister::class.java))
        }else{
            //Implementamos el handler que espera 3 segundos para cargar el activityMain
            Handler(Looper.getMainLooper()).postDelayed({
                //Iniciamos la actividad principal
                startActivity(Intent(this, MainActivity::class.java))
                //Acabamos la actividad

            }, 4000)
        }
        finish()
    }
}