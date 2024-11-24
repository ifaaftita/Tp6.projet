package com.example.projettp6.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "productDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE products (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                name TEXT, 
                price REAL, 
                description TEXT, 
                imageUrl INTEGER
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS products")
        onCreate(db)
    }

    // Insert a new product
    fun insertProduct(product: Product) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", product.name)
            put("price", product.price)
            put("description", product.description)
            put("imageUrl", product.imageUrl)
        }
        db.insert("products", null, values)
        db.close()
    }

    // Update an existing product by ID
    fun updateProduct(id: String, newName: String, newPrice: Double, newDescription: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", newName)
            put("price", newPrice)
            put("description", newDescription)
            // Assuming imageUrl is not updated, otherwise pass it as a parameter
        }
        // Update by ID, not by name
        db.update("products", values, "id = ?", arrayOf(id.toString()))
        db.close()
    }

    // Delete a product by ID
    fun deleteProduct(id: String) {
        val db = writableDatabase
        db.delete("products", "id = ?", arrayOf(id.toString()))
        db.close()
    }

    // Fetch a product by ID
    @SuppressLint("Range")
    fun getProductById(id: Long): Product? {
        val db = readableDatabase
        val cursor = db.query(
            "products",
            arrayOf("id", "name", "price", "description", "imageUrl"),
            "id = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        if (cursor != null && cursor.moveToFirst()) {
            val product = Product(
                cursor.getLong(cursor.getColumnIndex("id")).toString(),
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getDouble(cursor.getColumnIndex("price")).toString(),
                cursor.getString(cursor.getColumnIndex("description")),
                cursor.getInt(cursor.getColumnIndex("imageUrl"))
            )
            cursor.close()
            return product
        }
        cursor?.close()
        return null
    }
}