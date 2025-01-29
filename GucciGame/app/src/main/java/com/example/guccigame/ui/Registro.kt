package com.example.guccigame.ui

import ApiUsuarios
import DBHelper
import Usuarios
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guccigame.Login
import com.example.guccigame.LoginRegister
import com.example.guccigame.R
import com.example.guccigame.UsuariosSqlite
import org.w3c.dom.Text
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        cajaUsuario = findViewById(R.id.reg_username)
        cajaMail = findViewById(R.id.reg_email)


        val redirector = findViewById<TextView>(R.id.tv_login_redirect)
        redirector.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        //es la bandera que nos ayudará a determinar qué imagen se ve en la contraseña
        cajaPasswd = findViewById(R.id.et_password)
        val visualizador = findViewById<ImageView>(R.id.toggle_passwd)

        visualizador.setOnClickListener {
            bandera = !bandera

            if (bandera) {
                cajaPasswd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                visualizador.setImageResource(R.drawable.ocultarpasswd)
            } else {
                cajaPasswd.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                visualizador.setImageResource(R.drawable.mostrarpasswd)
            }
        }

        botonRegistro = findViewById(R.id.btn_register)

        botonRegistro.setOnClickListener {
            if (validarDatos()) {
                crearUsuario()
            } else {
                MostrarMensaje("Todo mal")
            }
        }


    }

    fun validarDatos(): Boolean {
        //verificamos los datos que nos introduce el usuario
        if (cajaUsuario.text.toString() == "") {
            MostrarMensaje("Introduzca el usuario!")
            return false
        }

        if (cajaMail.text.toString() == "" || (!cajaMail.text.toString()
                .endsWith("@gmail.com")) && cajaMail.text.toString().length > 11
        ) {
            MostrarMensaje("Introduzca un mail válido, y este debe pertenecer al dominio @gmail.com")
            return false
        }

        if (cajaPasswd.text.toString() == "" || cajaPasswd.text.toString().length < 8) {
            MostrarMensaje("La contraseña debe contener mínimo 8 caracteres!")
            return false
        }

        return true


    }

    fun crearUsuario() {
        var BASE_URL = "http://172.30.2.37/"
        var apiservice: ApiUsuarios

        //nos conectamos al servidor:
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

        apiservice = retrofit.create(ApiUsuarios::class.java)
        val nuevoUsuario = Usuarios(
            cajaUsuario.text.toString(),
            cajaMail.text.toString(),
            cajaPasswd.text.toString(),
            "Facil"
        )
        //EL NIVEL AL INICIO SIEMPRE VA A SER FÁCIL
        apiservice.registrarUsuario(nuevoUsuario).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    MostrarMensaje("Usuario registrado correctamente!!")
                    val dbHelper = DBHelper(this@Registro)
                    //abrimos la base de datos para escritura
                    val db = dbHelper.writableDatabase

                    //Instanciamos un usuario de sqlite
                    val usuarioSqlite = UsuariosSqlite(cajaMail.text.toString(), 0)

                    //insertamos al usuario instanciado
                    val valores = ContentValues().apply {
                        put("correo", usuarioSqlite.correo)
                        put("puntuacion",usuarioSqlite.puntuacion)
                    }

                    val insertResult = db.insert("Usuarios", null, valores)

                    //comprobamos que se ha insertado correctamente
                    if (insertResult != -1L) {
                        MostrarMensaje("Usuario registrado correctamente en SQLite también!")
                    } else {
                        MostrarMensaje("Error al registrar usuario en SQLite")
                    }
                }else{
                    MostrarMensaje("Error al registrar Usuario!")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                MostrarMensaje("Error de conexion: ${t.message}")
                t.printStackTrace()
            }
        })
    }

    fun MostrarMensaje(nota: String) {
        Toast.makeText(this, nota, Toast.LENGTH_LONG).show()
    }
}
