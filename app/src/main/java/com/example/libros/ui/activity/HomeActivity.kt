package com.example.libros.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libros.R
import com.example.libros.db.BooksDataBaseHelper
import com.example.libros.model.Book
import com.example.libros.ui.adapter.BookAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: BooksDataBaseHelper
    private lateinit var bookAdapter: BookAdapter
    private lateinit var userName: TextView
    private val bookList: MutableList<Book> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        dbHelper = BooksDataBaseHelper(this)

        userName = findViewById(R.id.nameHome)
        val buttonAddBook = findViewById<FloatingActionButton>(R.id.addBtnBooks)

        recyclerView = findViewById(R.id.recyclerViewBooks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Inicializa el adaptador con la lista vacía y lo asigna al RecyclerView
        bookAdapter = BookAdapter(bookList)
        recyclerView.adapter = bookAdapter

        // Obtener el usuario actual
        val user = auth.currentUser
        if (user != null) { // Si el nombre de usuario existe, lo muestra
            val displayName = user.displayName
            userName.text = "Hola, $displayName"
            loadBooks(user.uid)
        } else {
            userName.text = "Hola, Usuario"
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }

        //Botón para agregar libros: llevará a la activity de addBook
        buttonAddBook.setOnClickListener {
            val intent = Intent(this, AddBookActivity::class.java)
            startActivity(intent)
        }
    }
    private fun loadBooks(userId: String) {
        val books: List<Book> = dbHelper.getBooksByUser(userId)

        // Actualiza la lista de libros y notifica al adaptador
        if (books.isNotEmpty()) {
            bookList.clear()  // Limpia la lista actual
            bookList.addAll(books)  // Añade los nuevos libros
            bookAdapter.notifyDataSetChanged()  // Notifica al adaptador que los datos han cambiado
        } else {
            Toast.makeText(this, "No tienes libros guardados", Toast.LENGTH_SHORT).show()
        }
    }
}
