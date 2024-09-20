package com.example.libros.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.libros.model.Book


class BooksDataBaseHelper(context: Context): SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    companion object{
        //Datos de la base de datos
        private const val DATABASE_NAME = "books.db" //Nombre de la BD
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "books"
        private const val COLUM_ID = "id"
        private const val COLUM_TITLE = "title"
        private const val COLUM_AUTHOR = "author"
        private const val COLUM_STATE = "state"
        private const val COLUM_REVIEW = "review"
        private const val COLUM_USER_ID = "userId"
        private const val COLUM_IMAGE_PATH = "imagePath"
    }

    //Se crea la tabla en la base de datos
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($COLUM_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUM_TITLE TEXT, $COLUM_AUTHOR TEXT, $COLUM_STATE INTEGER, $COLUM_REVIEW TEXT, $COLUM_USER_ID TEXT, $COLUM_IMAGE_PATH TEXT)"
        db?.execSQL(createTableQuery)
    }
    //se manejan las actualizaciones en la base de datos
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }

    //Agregar libro
    fun insertBook(book:Book){
        val db = writableDatabase //Establezco la escritura de la base de datos

        //Llamo a los valores que se ingresarán
        val values = ContentValues().apply {
            put(COLUM_TITLE, book.title)
            put(COLUM_AUTHOR, book.author)
            put(COLUM_STATE,  if (book.state) 1 else 0)
            put(COLUM_REVIEW, book.review)
            put(COLUM_USER_ID, book.userId)
            put(COLUM_IMAGE_PATH, book.imagePath)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // Método para obtener libros por userId

    fun getBooksByUser(userId: String): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUM_USER_ID = ?", arrayOf(userId),
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUM_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUM_TITLE))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(COLUM_AUTHOR))
            val state = cursor.getInt(cursor.getColumnIndexOrThrow(COLUM_STATE)) == 1
            val review = cursor.getString(cursor.getColumnIndexOrThrow(COLUM_REVIEW))
            val userId = cursor.getString(cursor.getColumnIndexOrThrow(COLUM_USER_ID))
            val imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUM_IMAGE_PATH)) // Obtener el path de la imagen

            books.add(Book(id, title, author, state, review, userId,imagePath))
        }
        cursor.close()
        db.close()
        return books
    }
}