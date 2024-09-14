package com.example.libros.ui.activity

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
        val btnSingUp = findViewById<Button>(R.id.btnSignUp)


        //Se carga por defecto el login
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.containerFragment, loginFragment())
        }
        // Botón de login
        btnLogin.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.containerFragment, loginFragment())
            }
        }
        // Botón de Sing up
        btnSingUp.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.containerFragment, signUpFragment())
            }
        }
    }
}