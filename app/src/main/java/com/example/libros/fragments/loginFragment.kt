package com.example.libros.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.libros.R
import com.example.libros.HomeActivity
import com.google.firebase.auth.FirebaseAuth



class loginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth //Declaro la bd Auth para usarla mas adelante

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializo la bd Auth
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Llamo a los elementos del fragment
        val emailField = view.findViewById<EditText>(R.id.logEmail)
        val passwordField = view.findViewById<EditText>(R.id.logPassword)
        val loginButton = view.findViewById<Button>(R.id.btnLoginIng)

        //Al hacer click en el botón de login
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim() //Se obtine el texto, lo paso a string y luego se eliminan los espacios en blanco
            val password = passwordField.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        //Si se inicia sesión bien
                        if (task.isSuccessful) {
                            Log.d("Firebase", "Usuario autenticado: ${auth.currentUser?.email}")
                            Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                            // Lleva a la página Home
                            val intent = Intent(activity, HomeActivity::class.java)
                            startActivity(intent)
                            activity?.finish() // Finaliza la actividad para que no se pueda volver atrás.
                        } else {
                            // Falló el inicio de seción
                            Log.w("Firebase", "Error en el inicio de sesión", task.exception)
                            Toast.makeText(context, "Error en el inicio de sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

}