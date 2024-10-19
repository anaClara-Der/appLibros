package com.example.libros.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
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
import com.example.libros.ui.fragments.BookDetailsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: BooksDataBaseHelper
    private lateinit var bookAdapter: BookAdapter
    private lateinit var userName: TextView
    private lateinit var searchBooks: EditText
    private lateinit var rbAllBooks: RadioButton
    private lateinit var rbReadBooks: RadioButton
    private lateinit var rbUnreadBooks: RadioButton
    private val bookList: MutableList<Book> = mutableListOf() //Lista de libros
    private val filteredList: MutableList<Book> = mutableListOf() // Lista de libros filtrada


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Bases de datos
        auth = FirebaseAuth.getInstance()
        dbHelper = BooksDataBaseHelper(this)

    //Buscar elementos del layout
        userName = findViewById(R.id.nameHome)
        searchBooks = findViewById(R.id.searchBooks)
        rbAllBooks = findViewById(R.id.rbAllBooks)
        rbReadBooks = findViewById(R.id.rbReadBooks)
        rbUnreadBooks = findViewById(R.id.rbUnreadBooks)
        val filterBooksGroup = findViewById<RadioGroup>(R.id.filterBooksGroup)
        val buttonAddBook = findViewById<FloatingActionButton>(R.id.addBtnBooks)

        //Inicializar el recycler
        initRecycler()
        
        // Obtener el usuario actual
        val user = auth.currentUser
        if (user != null) { // Si el nombre de usuario existe, lo muestra
            val displayName = user.displayName
            userName.text = "Hola, $displayName"
            loadBooks(user.uid)
        } else { //Acá falta hacer que si no está registrado vuelva al inicio de sesión
            userName.text = "Hola, Usuario"
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }

        // Listener para el RadioGroup (todos, leídos, por leer)
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

        // TextWatcher para el buscador
        searchBooks.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterBooks(s.toString()) // función para filtar la lista de libros
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }



    //FUNCIONES

    // Inicializar el recycler
    private fun initRecycler() {
        recyclerView = findViewById(R.id.recyclerViewBooks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa el adaptador con la lista filtrada y lo asigna al RecyclerView
        bookAdapter = BookAdapter(filteredList, { book ->
            deleteBook(book) // Callback para eliminar
        }) { book, position ->
            showBookDetailsPopup(book, position) //  Mostrar detalles del libro
        }
        recyclerView.adapter = bookAdapter
    }
    //Crea el dialogo del fragment para mostrar el popup
    private fun showBookDetailsPopup(book: Book, position: Int) {
       // Log.i("Esta es la posicion del libro", "$position")
        val dialog = BookDetailsFragment.newInstance(

            book.title?: "Sin título disponible",
            book.author?: "Sin autor disponible",
            book.state,
            book.imagePath ?: "",  // Aquí se pasa la ruta de la imagen, en caso de ser null se pasa una cadena vacía
            book.review ?: "Sin reseña disponible", // Se asegura que review no sea null
            position,  // Se pasa la posicion de la card
            book.id
        )
        dialog.show(supportFragmentManager, "BookDetailsDialog")

    }

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
        bookList.clear()
        bookList.addAll(books)
        filteredList.clear()
        filteredList.addAll(books) // Actualizar también la lista filtrada
        bookAdapter.notifyDataSetChanged()
    }

    // Filtrar libros según el texto ingresado
    private fun filterBooks(query: String) {
        val filteredBooks = if (query.isEmpty()) {
            bookList // Mostrar todos los libros si no hay query
        } else {
            bookList.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.author.contains(query, ignoreCase = true)
            }
        }

        filteredList.clear()
        filteredList.addAll(filteredBooks)
        bookAdapter.notifyDataSetChanged()
    }
    // Eliminar libro
    private fun deleteBook(book: Book) {
        dbHelper.deleteBook(book.id) // Eliminar de la base de datos
        bookList.remove(book) // Eliminar de la lista COMPLETA
        filteredList.remove(book) // Elimina de la lista que se ve
        bookAdapter.notifyDataSetChanged() // Notificar al adaptador
        Toast.makeText(this, "Libro eliminado", Toast.LENGTH_SHORT).show()
    }
}
