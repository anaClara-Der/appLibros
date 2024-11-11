package com.example.libros.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.libros.R
import com.example.libros.ui.activity.HomeActivity
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

        if (auth.currentUser != null) {
            // El usuario está autenticado, redirige al HomeActivity
            val intent = Intent(activity, HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        // Llamo a los elementos del fragment
        val emailField = view.findViewById<EditText>(R.id.logEmail)
        val passwordField = view.findViewById<EditText>(R.id.logPassword)
        val loginButton = view.findViewById<Button>(R.id.btnLoginIng)
        //Validar que cumplan con las condiciones
        controlPassword(passwordField)
        controlEmail(emailField)

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

                            Toast.makeText(context, "Error en el inicio de sesión: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    //Funciones
    //Verifica que la contraseña tenga mas de 6 caracteres y menos de 20
    private fun controlPassword(passwordField: EditText) {
        passwordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.length in 6..20) {
                    // Cambia el ícono a violeta cuando cumple con los requisitos
                    passwordField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_ok, 0)
                } else {
                    // Muestra el ícono en gris si no cumple
                    passwordField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done, 0)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
    //Verificar que el mail cumpla con las condiciones
    private fun controlEmail(emailField: EditText) {
        emailField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString()
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_ok, 0)
                } else {
                    emailField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done, 0)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}