package com.example.guccigame

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guccigame.ui.Registro

class Login : AppCompatActivity() {
    lateinit var cajaMail: EditText
    lateinit var cajacontra: EditText
    lateinit var imagen : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val redirector = findViewById<TextView>(R.id.tv_register_redirect)
        redirector.setOnClickListener{
            startActivity(Intent(this, Registro::class.java))
        }

    }
}