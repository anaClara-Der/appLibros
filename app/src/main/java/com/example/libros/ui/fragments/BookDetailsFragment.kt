package com.example.libros.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.libros.R
import com.example.libros.ui.activity.AddBookActivity

// Constantes para los argumentos q se pasan al fragment
private const val ARG_TITLE = "title"
private const val ARG_AUTHOR = "author"
private const val ARG_STATE = "state"
private const val ARG_IMAGE_PATH = "imagePath"
private const val ARG_REVIEW = "review"
private const val ARG_POSITION = "position"
private const val ARG_BOOK_ID = "bookId"

// Este fragmento es un DialogFragment que mostrará los detalles del libro
class BookDetailsFragment : DialogFragment() {

    // Variables para guardar los datos
    private var title: String? = null
    private var author: String? = null
    private var state: Boolean = false
    private var imagePath: String? = null
    private var review: String? = null
    private var position: Int? = null
    private var bookId: Int? = null

    // Método que crea una nueva instancia del fragmento con los argumentos necesarios
    companion object {
        fun newInstance(title: String, author: String, state: Boolean, imagePath: String, review: String, position: Int, bookId: Int): BookDetailsFragment {
            val fragment = BookDetailsFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_AUTHOR, author)
            args.putBoolean(ARG_STATE, state)
            args.putString(ARG_IMAGE_PATH, imagePath)
            args.putString(ARG_REVIEW, review)
            args.putInt(ARG_POSITION, position)
            args.putInt(ARG_BOOK_ID, bookId)
            fragment.arguments = args
            return fragment
        }
    }

    //Se recuperan los datos pasados como argumentos
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            author = it.getString(ARG_AUTHOR)
            state = it.getBoolean(ARG_STATE)
            imagePath = it.getString(ARG_IMAGE_PATH)
            review = it.getString(ARG_REVIEW)
            position = it.getInt(ARG_POSITION)
            bookId = it.getInt(ARG_BOOK_ID)
        }
    }

    // Tamaño del fragment y la oscuridad del fondo
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.8).toInt()
        dialog?.window?.setLayout(width, height)

        dialog?.window?.setDimAmount(0.5f) //Para que el fondo quede un poco más oscuro que el fragment
    }

    // Inflar el layout del fragmento y mostrar los datos
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_book_details, container, false)

        // Asigna los valores a los elementos del layout
        val titleTextView = view.findViewById<TextView>(R.id.titleFragment)
        val authorTextView = view.findViewById<TextView>(R.id.authorFragment)
        val stateTextView = view.findViewById<TextView>(R.id.stateFragment)
        val reviewTextView = view.findViewById<TextView>(R.id.reviewFragment)
        val imageView = view.findViewById<ImageView>(R.id.imgFrag)
        val imgCloseFragment = view.findViewById<ImageView>(R.id.closeFragent)
        val editImageView = view.findViewById<ImageView>(R.id.editFragment)

        // Muestra los detalles
        titleTextView.text = "Título: $title"
        authorTextView.text = "Autor: $author"
        stateTextView.text = "Estado: ${if (state) "Leído" else "No leído"}"
        reviewTextView.text = "Reseña: $review"

        // Cargar la imagen usando Glide
        Glide.with(requireContext())
            .load(imagePath)
            .placeholder(R.drawable.upload_img)
            .error(R.drawable.error_image)
            .into(imageView)
        //Cerrar el fragment
        imgCloseFragment.setOnClickListener {
            dismiss()
        }

        // Abrir AddBookActivity al hacer clic en el ícono de edición
        editImageView.setOnClickListener {
            val intent = Intent(requireContext(), AddBookActivity::class.java)

            // Pasar los datos actuales del libro a la actividad de edición
            intent.putExtra("title", title)
            intent.putExtra("author", author)
            intent.putExtra("state", state)
            intent.putExtra("imagePath", imagePath)
            intent.putExtra("review", review)
            intent.putExtra("position", position)  // Pasar la posición
            intent.putExtra("bookId", bookId)
            startActivity(intent)  // Abre la actividad para editar
        }

        return view
    }
}
