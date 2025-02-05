package com.example.guccigame.ui

import ApiUsuarios
import DBHelper
import Usuarios
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.guccigame.Login
import com.example.guccigame.R
import com.example.guccigame.UsuariosSqlite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Registro : AppCompatActivity() {
    lateinit var cajaPasswd: EditText
    lateinit var cajaUsuario: EditText
    lateinit var cajaMail: EditText
    private var bandera: Boolean = false
    lateinit var botonRegistro: Button
    private lateinit var dbHelper: DBHelper  // Declaramos DBHelper a nivel de clase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        cajaUsuario = findViewById(R.id.reg_username)
        cajaMail = findViewById(R.id.reg_email)
        cajaPasswd = findViewById(R.id.et_password)
        botonRegistro = findViewById(R.id.btn_register)

        // Inicializamos DBHelper
        dbHelper = DBHelper(this)  // Ahora se inicializa una vez en onCreate

        val redirector = findViewById<TextView>(R.id.tv_login_redirect)
        redirector.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        val visualizador = findViewById<ImageView>(R.id.toggle_passwd)
        visualizador.setOnClickListener {
            bandera = !bandera
            cajaPasswd.inputType = if (bandera) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            visualizador.setImageResource(
                if (bandera) R.drawable.ocultarpasswd else R.drawable.mostrarpasswd
            )
        }

        botonRegistro.setOnClickListener {
            //SI le damos al click del boton, validamos si se puede insertar o no!
            if (validarDatos()) {
                crearUsuario()
            } else {
                mostrarMensaje("Introduzca los datos correctamente por favor!")
            }
        }
    }

    fun validarDatos(): Boolean {
        if (cajaUsuario.text.toString().trim().isEmpty()) {
            mostrarMensaje("Introduzca el usuario!")
            return false
        }

        if (cajaMail.text.toString().trim().isEmpty() ||
            (!cajaMail.text.toString().endsWith("@gmail.com")) && cajaMail.text.toString().length > 11
        ) {
            mostrarMensaje("Introduzca un mail válido, y este debe pertenecer al dominio @gmail.com")
            return false
        }

        if (cajaPasswd.text.toString().trim().isEmpty() || cajaPasswd.text.toString().length < 8) {
            mostrarMensaje("La contraseña debe contener mínimo 8 caracteres!")
            return false
        }

        return true
    }

    fun crearUsuario() {
        val BASE_URL = "http://172.30.2.142/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiUsuarios::class.java)

        val nuevoUsuario = Usuarios(
            cajaUsuario.text.toString(),
            cajaMail.text.toString(),
            cajaPasswd.text.toString(),
            "Facil"
        )

        val call: Call<Void> = apiService.registrarUsuario(nuevoUsuario)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    mostrarMensaje("Se ha registrado correctamente")

                    // Guardar el usuario también en SQLite
                    guardarEnSQLite(nuevoUsuario)

                    // Guardar el usuario en SharedPreferences
                    guardarEnSharedPreferences(nuevoUsuario.nombre, nuevoUsuario.passwd, 0)

                    val Intent = Intent(this@Registro, Login::class.java)

                } else {
                    mostrarMensaje("Error al registrar en la base externa.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                mostrarMensaje("No se pudo insertar en la base externa: ${t.message}")
            }
        })
    }

    fun guardarEnSQLite(usuario: Usuarios) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("correo", usuario.correo)
            put("nombre", usuario.nombre)
            put("password", usuario.passwd)
            put("puntuacion", 0)
        }

        val newRowId = db.insert("Usuarios", null, values)
        if (newRowId != -1L) {
            mostrarMensaje("Usuario guardado en SQLite correctamente!")

        } else {
            mostrarMensaje("Error al guardar en SQLite.")
        }
    }

    fun guardarEnSharedPreferences(usuario: String, password: String, puntuacion: Int) {
        val sharedPref = getSharedPreferences("mis_preferencias", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("Usuario", usuario)
        editor.putString("Passwd", password)
        editor.putInt("Puntuacion", puntuacion)
        editor.apply()
    }

    fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
