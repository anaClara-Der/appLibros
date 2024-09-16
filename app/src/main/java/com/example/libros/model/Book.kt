package com.example.libros.model

import android.net.Uri

data class Book (
    val id: Int,
    val title: String,
    val author: String,
    val state:Boolean,
    val review:String
    )