package com.example.libros.ui.activity


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.libros.R
import com.example.libros.db.BooksDataBaseHelper
import com.example.libros.model.Book
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth


class AddBookActivity : AppCompatActivity() {
    private lateinit var dbHelper: BooksDataBaseHelper
    private lateinit var saveButton: Button
    private lateinit var titleEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var stateSpinner: Spinner
    private lateinit var reviewEditText: EditText
    private lateinit var image:ImageView
    private lateinit var closeImg:ImageView
    private var imageUri: Uri? = null
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_book)
        // Inicializa la base de datos sqlite
        dbHelper = BooksDataBaseHelper(this)
        auth = FirebaseAuth.getInstance()

        // Se inicializan los componentes del layout
        titleEditText = findViewById(R.id.addTitle)
        authorEditText = findViewById(R.id.addAuthor)
        stateSpinner = findViewById(R.id.spinnerState)
        reviewEditText = findViewById(R.id.addReview)
        saveButton = findViewById(R.id.add)
        image = findViewById(R.id.bookImage)
        closeImg =  findViewById(R.id.close)
        //Se configura el spinner con los dos valores
        stateSpinner()

        // Se configura el botón para guardar
        saveButton.setOnClickListener {
            saveBook()
        }
        //Se toma la fotografía de la cámara o la galería
        image.setOnClickListener{
            pickImage()
        }
        //Al precionar la X para salir de la página sin guardar nada
        closeImg.setOnClickListener{
            closeActivity()
        }


    }
    //Agregar una imagen
    private val  resultImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
                if(result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    imageUri = data!!.data
                    image.setImageURI(imageUri)
                }else{
                    Toast.makeText(this, "Imagen no seleccionada", Toast.LENGTH_SHORT).show()
                }
        }


    //FUNCIONES
    //Función para agregar estados al spinner: leído o no leído
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
    //Función para seleccionar imagen
    private fun pickImage(){
        ImagePicker.with(this)
            .crop() //Para recortar la imagen
            .compress(1024) //Para comprimir la imagen menor a 1mb
            .maxResultSize(1080,1080) //La resolución será menor a esto
            .createIntent { intent ->
                resultImg.launch(intent)
            }
    }
    //Función al clickear en la X
    private fun closeActivity(){
        finish()
    }
    //Función al clickear el botón guardar: se guarda o se edita un libro existente
    private fun saveBook(){
        val title = titleEditText.text.toString()
        val author = authorEditText.text.toString()
        val review = reviewEditText.text.toString()
        val state = when (stateSpinner.selectedItem.toString()) {
            "Leído" -> true
            "Por leer" -> false
            else -> false
        }
        val userId = auth.currentUser?.uid ?: ""
        val imagePath = imageUri?.path // Obtener el path de la imagen


        if(title.isNotEmpty() or author.isNotEmpty() or review.isNotEmpty()){
            val bookId = intent.getIntExtra("bookId", -1)
            // Creo un  un objeto Book del tipo Book
                val book = Book(
                    id = 0, // El ID se genera automáticamente
                    title = title,
                    author = author,
                    state = state,
                    review = review,
                    userId = userId,
                    imagePath = imagePath
                )
                dbHelper.insertBook(book)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Libro guardado", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Es necesario que completes todos los campos", Toast.LENGTH_SHORT).show()
        }

    }
    //Función para editar lo que se pase en el intent del fragment
    private fun editBook(){
        // Verificar si los datos han sido pasados desde el fragment
        val bookTitle = intent.getStringExtra("title")
        val bookAuthor = intent.getStringExtra("author")
        val bookState = intent.getBooleanExtra("state", false)
        val bookImagePath = intent.getStringExtra("imagePath")
        val bookReview = intent.getStringExtra("review")

        // Si los datos están presentes, rellenar los campos con ellos
        if (bookTitle != null && bookAuthor != null && bookReview != null) {
            titleEditText.setText(bookTitle)
            authorEditText.setText(bookAuthor)
            reviewEditText.setText(bookReview)
            stateSpinner.setSelection(if (bookState) 0 else 1)  // Seleccionar "Leído" o "Por leer"

            // Si hay imagen, mostrarla
            if (bookImagePath != null) {
                imageUri = Uri.parse(bookImagePath)
                image.setImageURI(imageUri)
            }
        }
    }
}
