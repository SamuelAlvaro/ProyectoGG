package com.example.guccigame

import DBHelper
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.guccigame.ui.Registro

class Login : AppCompatActivity() {
    lateinit var cajaUsuario: EditText
    lateinit var cajaContra: EditText
    lateinit var imagenOjo: ImageView
    lateinit var bntLogin: Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        cajaUsuario = findViewById(R.id.et_user_Login)
        cajaContra = findViewById(R.id.et_password_Login)
        bntLogin = findViewById(R.id.btn_login)

        sharedPreferences = getSharedPreferences("mis_preferencias", MODE_PRIVATE)
        dbHelper = DBHelper(this) // Tu clase SQLiteOpenHelper


        val redirector = findViewById<TextView>(R.id.tv_register_redirect)
        redirector.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }

        val imagenOjo = findViewById<ImageView>(R.id.toggle_passwd_login) // Suponiendo que el botón sea una imagen

        // Este bloque de código debería ejecutarse cuando el usuario presione el botón de inicio de sesión
        bntLogin.setOnClickListener {
            if (cajaUsuario.text.toString().trim().isEmpty() || cajaContra.text.toString().trim().isEmpty()) {
                mostrarToast("Tienes que rellenar todos los campos")
            } else {
                validarCredenciales()
            }
        }
    }

    fun validarCredenciales() {
        val usuario = cajaUsuario.text.toString().trim()
        val password = cajaContra.text.toString().trim()

        val usuarioGuardado = sharedPreferences.getString("Usuario", "")
        val passwordGuardada = sharedPreferences.getString("Passwd", "")

        if (usuario == usuarioGuardado && password == passwordGuardada) {
            mostrarToast("Inicio de sesión exitoso!!")
            startActivity(Intent(this@Login, MainActivity::class.java))
            finish()
            return
        }

        if (dbHelper.validarCredenciales(usuario, password)) {
            actualizarSharedPreferences(usuario, password)
            mostrarToast("Datos de usuario actualizados en SharedPreferences")
            startActivity(Intent(this@Login, MainActivity::class.java))
            finish()
        } else {
            mostrarToast("Usuario o contraseña incorrectos!!")
        }
    }




    fun actualizarSharedPreferences(usuario: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("Usuario", usuario)
        editor.putString("Passwd", password)
        editor.putInt("Puntuacion", 0) // Reinicia puntuación
        editor.apply()
    }

    fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
