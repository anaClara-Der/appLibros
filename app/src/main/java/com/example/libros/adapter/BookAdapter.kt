package com.example.libros.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libros.R
import com.example.libros.model.Book

//Se ocupa de tomar la información de la bd y agregarla a la recycler
class BookAdapter(
    private val bookList: MutableList<Book>,
    private val onBookDeleted: (Book) -> Unit,
    private val onBookClicked: (Book, Int) -> Unit ) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewTitle)
        val author: TextView = itemView.findViewById(R.id.textViewAuthor)
        val state: TextView = itemView.findViewById(R.id.textViewState)
        val imageView: ImageView = itemView.findViewById(R.id.imageBook)
        val deleteButton: ImageView = itemView.findViewById(R.id.btnDelete)
        // Agrega el listener para la card
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onBookClicked(bookList[position], position) // Pasar el libro y la posición
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]

        Log.i("Esta es la posicion" , "$position")
        holder.title.text = book.title
        holder.author.text = " ${book.author}"

        holder.deleteButton.setOnClickListener {
            onBookDeleted(book)
        }
        // Cargar la imagen usando Glide desde la ruta local
        Glide.with(holder.itemView.context)
            .load(book.imagePath)
            .placeholder(R.drawable.upload_img)
            .error(R.drawable.error_image)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = bookList.size

    fun updateBookAtPosition(book: Book, position: Int) {
        bookList[position] = book // Actualiza la lista en la posición especificada
        notifyItemChanged(position) // Notifica al adaptador que el ítem ha cambiado
    }
}
