package com.example.libros.ui.activity

import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import com.example.libros.R
import com.example.libros.ui.fragments.loginFragment
import com.example.libros.ui.fragments.signUpFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

       val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        // Configurar el color inicial
        btnLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
        btnSignUp.setBackgroundColor(ContextCompat.getColor(this, R.color.second))

        //Se carga por defecto el login
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.containerFragment, loginFragment())
        }
        // Botón de login
        btnLogin.setOnClickListener {
            btnLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            btnSignUp.setBackgroundColor(ContextCompat.getColor(this, R.color.second))
            //tamaño y el estilo del texto
            btnLogin.setTextSize(18f)
            btnLogin.setTypeface(null, Typeface.BOLD)

            btnSignUp.setTextSize(16f)
            btnSignUp.setTypeface(null, Typeface.NORMAL)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.containerFragment, loginFragment())
            }
        }
        // Botón de Sing up
        btnSignUp.setOnClickListener {
            btnSignUp.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            btnLogin.setBackgroundColor(ContextCompat.getColor(this, R.color.second))
            //  tamaño y el estilo del texto
            btnSignUp.setTextSize(18f)
            btnSignUp.setTypeface(null, Typeface.BOLD)

            btnLogin.setTextSize(16f)
            btnLogin.setTypeface(null, Typeface.NORMAL)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.containerFragment, signUpFragment())
            }
        }
    }
}