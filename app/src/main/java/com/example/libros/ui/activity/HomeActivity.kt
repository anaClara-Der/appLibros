package com.example.libros.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.libros.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        val userName = findViewById<TextView>(R.id.nameHome)
        val buttonAddBook = findViewById<FloatingActionButton>(R.id.addBtnBooks)

        // Obtener el usuario actual
        val user = auth.currentUser
        if (user != null) {
            val displayName = user.displayName
            // Mostrar el nombre del usuario en el TextView
            userName.text = "Hola, $displayName"
        } else {
            userName.text = "Hola, Usuario"
        }

        //Botón para agregar libros
        buttonAddBook.setOnClickListener {
            // Lógica para añadir un nuevo libro

            val intent = Intent(this, AddBookActivity::class.java)
            startActivity(intent)
        }
    }
}
