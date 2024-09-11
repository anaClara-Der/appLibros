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
import com.google.firebase.firestore.FirebaseFirestore

class signUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar Firebase Auth y Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sing_up, container, false)

        // Obtener referencias de los campos
        val emailField = view.findViewById<EditText>(R.id.signEmail)
        val passwordField = view.findViewById<EditText>(R.id.signPassword)
        val nameField = view.findViewById<EditText>(R.id.signName)
        val signUpButton = view.findViewById<Button>(R.id.btnSignUpIng)

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

                            // Crear la colección de libros vacía en Firestore para este usuario
                            val userId = user?.uid
                            val userBooksRef = db.collection("users").document(userId!!).collection("libros")

                            // Inicializar la colección sin libros
                            userBooksRef.document().set(emptyMap<String, Any>())
                                .addOnSuccessListener {
                                    Log.d("Firebase", "Colección de libros creada para el usuario: $userId")

                                    // Redirigir a la HomeActivity
                                    val intent = Intent(activity, HomeActivity::class.java)
                                    startActivity(intent)
                                    activity?.finish() // Opcional: Finalizar la actividad actual para que no se pueda volver atrás
                                }
                                .addOnFailureListener { e ->
                                    Log.w("Firebase", "Error al crear la colección de libros", e)
                                    Toast.makeText(context, "Error al crear los datos del usuario", Toast.LENGTH_SHORT).show()
                                }

                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        } else {
                            // Si el registro falla, muestra un mensaje
                            Log.w("Firebase", "Error en el registro", task.exception)
                            Toast.makeText(context, "Error en el registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
