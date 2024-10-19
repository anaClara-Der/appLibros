package com.example.libros.model

import android.net.Uri

data class Book (
    val id: Int,
    var title: String,
    var author: String,
    var state:Boolean,
    var review:String,
    val userId: String,
    var imagePath: String?
    )