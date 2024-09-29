package com.example.libros.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
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
    private lateinit var rbAllBooks: RadioButton
    private lateinit var rbReadBooks: RadioButton
    private lateinit var rbUnreadBooks: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    //Bases de datos
        auth = FirebaseAuth.getInstance()
        dbHelper = BooksDataBaseHelper(this)

    //Layout
        userName = findViewById(R.id.nameHome)
        val buttonAddBook = findViewById<FloatingActionButton>(R.id.addBtnBooks)
    //Inicializar el recycler
        initRecycler()
        
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
    //Inicializar los radioButtons
        rbAllBooks = findViewById(R.id.rbAllBooks)
        rbReadBooks = findViewById(R.id.rbReadBooks)
        rbUnreadBooks = findViewById(R.id.rbUnreadBooks)

        // Listener para el RadioGroup
        val filterBooksGroup = findViewById<RadioGroup>(R.id.filterBooksGroup)
        filterBooksGroup.setOnCheckedChangeListener { _, checkedId ->
            val userId = user?.uid ?: ""
            when (checkedId) {
                R.id.rbAllBooks -> loadBooks(userId) // Cargar todos los libros
                R.id.rbReadBooks -> loadBooksByState(userId, true) // Cargar solo leídos
                R.id.rbUnreadBooks -> loadBooksByState(userId, false) // Cargar solo no leídos
            }
        }

        //Botónpara agregar libros: llevará a la activity de addBook
        buttonAddBook.setOnClickListener {
            val intent = Intent(this, AddBookActivity::class.java)
            startActivity(intent)
        }
    }
//Funciones
    //Inicializar recycler
    private fun initRecycler(){
        recyclerView = findViewById(R.id.recyclerViewBooks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa el adaptador con la lista vacía y lo asigna al RecyclerView
        bookAdapter = BookAdapter(bookList){ book ->
            deleteBook(book) // Callback para eliminar
        }
        recyclerView.adapter = bookAdapter
    }
//Cargar todos los libro
// Cargar todos los libros
private fun loadBooks(userId: String) {
    val books: List<Book> = dbHelper.getBooksByUser(userId)
    updateBookList(books)
}

    // Cargar libros por estado
    private fun loadBooksByState(userId: String, isRead: Boolean) {
        val books: List<Book> = dbHelper.getBooksByUser(userId).filter { it.state == isRead }
        updateBookList(books)
    }

    // Actualizar la lista de libros en el RecyclerView
    private fun updateBookList(books: List<Book>) {
        val previousSize = bookList.size
        bookList.clear()
        bookAdapter.notifyItemRangeRemoved(0, previousSize)
        bookList.addAll(books)
        bookAdapter.notifyItemRangeInserted(0, books.size)
    }

    // Eliminar libro
    private fun deleteBook(book: Book) {
        dbHelper.deleteBook(book.id) // Eliminar de la base de datos
        bookList.remove(book) // Eliminar de la lista
        bookAdapter.notifyDataSetChanged() // Notificar al adaptador
        Toast.makeText(this, "Libro eliminado", Toast.LENGTH_SHORT).show()
    }
}
