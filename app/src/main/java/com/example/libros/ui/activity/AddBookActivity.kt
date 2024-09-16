package com.example.libros.ui.activity


import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.libros.R
import com.example.libros.db.BooksDataBaseHelper
import com.example.libros.model.Book


class AddBookActivity : AppCompatActivity() {
    private lateinit var dbHelper: BooksDataBaseHelper
    private lateinit var saveButton: ImageView
    private lateinit var titleEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var stateSpinner: Spinner
    private lateinit var reviewEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_book)
        dbHelper = BooksDataBaseHelper(this)// Se inicializa la base de datos

        // Se inicializan los componentes
        titleEditText = findViewById(R.id.addTitle)
        authorEditText = findViewById(R.id.addAuthor)
        stateSpinner = findViewById(R.id.spinnerState)
        reviewEditText = findViewById(R.id.addReview)
        saveButton = findViewById(R.id.add)


        stateSpinner() //Se configura el spinner con los dos valores

        saveButton.setOnClickListener { // Se configura el botón para guardar
            saveBook()
        }
    }


    //Función para agregar estados al spinner. Leído o no leído
    private fun stateSpinner(){
        val ratingSpinner = findViewById<Spinner>(R.id.spinnerState)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.state_array,
            android.R.layout.simple_spinner_item
        )
        // Por defecto se muestra el mismo.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ratingSpinner.adapter = adapter
    }
    //Función para acceder a los componentes del layaout
    private fun saveBook(){

        val title = titleEditText.text.toString()
        val author = authorEditText.text.toString()
        val review = reviewEditText.text.toString()
        val state = when (stateSpinner.selectedItem.toString()) {
            "Leído" -> true
            "Por leer" -> false
            else -> false
        }
        // Creo un  un objeto Book del tipo Book
        val book = Book(
            id = 0, // El ID se genera automáticamente
            title = title,
            author = author,
            state = state,
            review = review,

        )
        // Insertar el libro en la base de datos
        dbHelper.insertBook(book)

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finishAffinity()
        Toast.makeText(this, "Libro guardado", Toast.LENGTH_SHORT).show()
    }

}
