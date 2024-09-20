package com.example.libros.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libros.R
import com.example.libros.model.Book

class BookAdapter(private val bookList: List<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewTitle)
        val author: TextView = itemView.findViewById(R.id.textViewAuthor)
        val state: TextView = itemView.findViewById(R.id.textViewState)
        val imageView: ImageView = itemView.findViewById(R.id.imageBook)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.title.text = book.title
        holder.author.text = book.author
        holder.state.text = if (book.state) "Leído" else "Por leer"

        // Cargar la imagen usando Glide desde la ruta local
        Glide.with(holder.itemView.context)
            .load(book.imagePath)
            .placeholder(R.drawable.upload_img)
            .error(R.drawable.error_image)
            .into(holder.imageView)
    }

    override fun getItemCount() = bookList.size
}
