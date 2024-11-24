package com.example.projettp6

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.projettp6.data.DatabaseHelper

@Composable
fun PageInfo(navController: NavHostController, productId: Long) {
    val dbHelper = DatabaseHelper(navController.context)

    // Fetch product details by ID from the database
    val product = dbHelper.getProductById(productId)
    if (product == null) {
        // Show an error message if product is not found
        Toast.makeText(navController.context, "Product not found", Toast.LENGTH_SHORT).show()
        return
    }

    // States for the editable fields
    var newName by remember { mutableStateOf(product.name) }
    var newPrice by remember { mutableStateOf(product.price.toString()) }
    var newDescription by remember { mutableStateOf(product.description) }

    // Layout for displaying and editing product details
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Product Info", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Product Name Field
        OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Product Price Field
        OutlinedTextField(
            value = newPrice,
            onValueChange = { newPrice = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Product Description Field
        OutlinedTextField(
            value = newDescription,
            onValueChange = { newDescription = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            // Update Product Button
            Button(
                onClick = {
                    val updatedPrice = newPrice.toDoubleOrNull() ?: 0.0
                    dbHelper.updateProduct(product.id, newName, updatedPrice, newDescription)
                    navController.popBackStack() // Navigate back after update
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text("Update Product")
            }

            // Delete Product Button
            Button(
                onClick = {
                    dbHelper.deleteProduct(product.id)
                    navController.popBackStack() // Navigate back after delete
                }
            ) {
                Text("Delete Product")
            }
        }
    }
}


