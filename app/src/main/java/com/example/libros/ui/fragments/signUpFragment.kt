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
import com.google.firebase.auth.UserProfileChangeRequest

class signUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sing_up, container, false)

        // Referencias de los elementos visuales
        val emailField = view.findViewById<EditText>(R.id.signEmail)
        val passwordField = view.findViewById<EditText>(R.id.signPassword)
        val nameField = view.findViewById<EditText>(R.id.signName)
        val signUpButton = view.findViewById<Button>(R.id.btnSignUpIng)

    //Validar que cumplan con las condiciones
        controlPassword(passwordField)
        controlName(nameField)
        controlEmail(emailField)

    //Al hacer click en el botón de Sing up
        signUpButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // El usuario se registró correctamente
                            val user = auth.currentUser
                            Log.d("Firebase", "User registered: ${user?.email}")

                            // Actualizar el perfil del usuario con el nombre
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()

                            user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { profileUpdateTask ->
                                    if (profileUpdateTask.isSuccessful) {
                                        Log.d("Firebase", "User profile updated.")
                                        // Manda a la HomeActivity
                                        val intent = Intent(activity, HomeActivity::class.java)
                                        startActivity(intent)
                                        activity?.finish() // Termina la actividad actual para que no se pueda volver atrás

                                        Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.w("Firebase", "Error en la actualización del perfil", profileUpdateTask.exception)
                                        Toast.makeText(context, "Error al actualizar el perfil: ${profileUpdateTask.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }
                                }
                        } else {
                            // Si el registro falla, muestra un mensaje
                            Log.w("Firebase", "Error en el registro", task.exception)
                            Toast.makeText(context, "Error en el registro: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
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
    //Verificar que el nombre de usuario cumpla con las condiciones
    private fun controlName(nameField: EditText) {
        nameField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val name = s.toString()
                if (name.length >= 3) { // Puedes ajustar la longitud mínima según necesites
                    nameField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_user_ok, 0)
                } else {
                    nameField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_user, 0)
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
