package com.example.libros.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.libros.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val buttonAddBook = findViewById<FloatingActionButton>(R.id.addBtnBooks)
        buttonAddBook.setOnClickListener {
            // Lógica para añadir un nuevo libro

            val intent = Intent(this, AddBookActivity::class.java)
            startActivity(intent)
        }
    }
}
