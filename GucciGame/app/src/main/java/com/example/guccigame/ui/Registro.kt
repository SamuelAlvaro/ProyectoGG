package com.example.guccigame.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.guccigame.LoginRegister
import com.example.guccigame.R
import org.w3c.dom.Text

class Registro : AppCompatActivity() {
    lateinit var cajaPasswd: EditText
    lateinit var cajaUsuario: EditText
    lateinit var cajaMail: EditText
    private var bandera: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        val redirector = findViewById<TextView>(R.id.tv_login_redirect)
        redirector.setOnClickListener{
            startActivity(Intent(this, LoginRegister::class.java))
        }
        //es la bandera que nos ayudará a determinar qué imagen se ve en la contraseña

        cajaPasswd=findViewById(R.id.et_password)
        val visualizador=findViewById<ImageView>(R.id.toggle_passwd)

        visualizador.setOnClickListener{
            bandera = !bandera

            if(bandera){
                cajaPasswd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                visualizador.setImageResource(R.drawable.ocultarpasswd)
            } else{
                cajaPasswd.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                visualizador.setImageResource(R.drawable.mostrarpasswd)
            }
        }



    }
}