package com.example.projettp6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projettp6.data.DatabaseHelper
import com.example.projettp6.data.Product
import com.example.projettp6.ui.theme.ProjetTP6Theme

class Homepage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetTP6Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "product_list") {
                    composable("product_list") { ListComposable(navController) }
                    composable("product_detail/{id}/{name}/{price}/{description}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
                        val name = backStackEntry.arguments?.getString("name")
                        val price = backStackEntry.arguments?.getString("price")
                        val description = backStackEntry.arguments?.getString("description")
                        ProductDetailScreen(id = id, name = name, price = price, description = description, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ListComposable(navController: NavHostController) {
    val list = remember {
        mutableStateListOf(
            Product(1.toString(), "Kiko Gloss", "10.8", "Description 1", R.drawable.prod1),
            Product(2.toString(), "Rouge à Lèvre", "20.0", "Description 2", R.drawable.prod2),
            Product(3.toString(), "Hightlighter", "15.0", "Description 3", R.drawable.prod3),
            Product(4.toString(), "Blush Gloss", "12.5", "Description 4", R.drawable.prod4),
            Product(5.toString(), "Palette de Fards à Paupières", "18.3", "Description 5", R.drawable.prod5),
            Product(6.toString(), "Fond de Teint", "14.2", "Description 6", R.drawable.prod6)
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(list) { product ->
            ProductCard(navController = navController, product = product)
        }
    }
}

@Composable
fun ProductCard(navController: NavHostController, product: Product, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable {
                navController.navigate("product_detail/${product.id}/${product.name}/${product.price}/${product.description}")
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = product.imageUrl),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = product.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Price: ${product.price} €",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ProductDetailScreen(id: Long, name: String?, price: String?, description: String?, navController: NavHostController) {
    var newName by remember { mutableStateOf(name ?: "") }
    var newPrice by remember { mutableStateOf(price ?: "") }
    var newDescription by remember { mutableStateOf(description ?: "") }
    val dbHelper = DatabaseHelper(navController.context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Product Name", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = newName,
            onValueChange = { newName = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newPrice,
            onValueChange = { newPrice = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = newDescription,
            onValueChange = { newDescription = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            // Update Product Button
            Button(onClick = {
                dbHelper.updateProduct(id.toString(), newName, newPrice.toDoubleOrNull() ?: 0.0, newDescription)
                navController.popBackStack() // Navigate back after update
            }) {
                Text("Update Product")
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Delete Product Button
            Button(onClick = {
                dbHelper.deleteProduct(id.toString())
                navController.popBackStack() // Navigate back after delete
            }) {
                Text("Delete Product")
            }
        }
    }
}
