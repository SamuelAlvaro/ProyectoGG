package com.example.guccigame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guccigame.ui.Registro

class LoginRegister : AppCompatActivity() {
    lateinit var botonLogin: Button
    lateinit var  botonRegistro: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_register)
        botonRegistro = findViewById(R.id.btnRegistrar)
        botonRegistro.setOnClickListener{
            startActivity(Intent(this, Registro::class.java))
            finish()
        }
        botonLogin = findViewById(R.id.btnLogin)
        botonLogin.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
            finish()
        }


    }
}