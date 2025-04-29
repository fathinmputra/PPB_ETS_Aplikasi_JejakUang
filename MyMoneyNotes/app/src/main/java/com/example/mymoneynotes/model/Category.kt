package com.example.mymoneynotes.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class CategoryType {
    INCOME, EXPENSE
}

data class Category(
    val id: Int,
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val type: CategoryType
)

// Kategori predefined untuk aplikasi
object Categories {
    // Kategori Pemasukan
    val Salary = Category(1, "Gaji", Icons.Default.AttachMoney, Color(0xFF4CAF50), CategoryType.INCOME)
    val Investment = Category(2, "Investasi", Icons.Default.TrendingUp, Color(0xFF2196F3), CategoryType.INCOME)
    val Gift = Category(3, "Hadiah", Icons.Default.CardGiftcard, Color(0xFF9C27B0), CategoryType.INCOME)
    val Other_Income = Category(4, "Lainnya", Icons.Default.Add, Color(0xFF607D8B), CategoryType.INCOME)

    // Kategori Pengeluaran
    val Food = Category(5, "Makanan", Icons.Default.Restaurant, Color(0xFFFF5722), CategoryType.EXPENSE)
    val Transport = Category(6, "Transportasi", Icons.Default.DirectionsCar, Color(0xFFFFC107), CategoryType.EXPENSE)
    val Shopping = Category(7, "Belanja", Icons.Default.ShoppingCart, Color(0xFFE91E63), CategoryType.EXPENSE)
    val Bills = Category(8, "Tagihan", Icons.Default.Receipt, Color(0xFF3F51B5), CategoryType.EXPENSE)
    val Entertainment = Category(9, "Hiburan", Icons.Default.Theaters, Color(0xFF009688), CategoryType.EXPENSE)
    val Health = Category(10, "Kesehatan", Icons.Default.LocalHospital, Color(0xFFF44336), CategoryType.EXPENSE)
    val Education = Category(11, "Pendidikan", Icons.Default.School, Color(0xFF795548), CategoryType.EXPENSE)
    val Other_Expense = Category(12, "Lainnya", Icons.Default.More, Color(0xFF607D8B), CategoryType.EXPENSE)

    // List semua kategori
    val allCategories = listOf(
        Salary, Investment, Gift, Other_Income,
        Food, Transport, Shopping, Bills, Entertainment, Health, Education, Other_Expense
    )

    // Kategori berdasarkan tipe
    val incomeCategories = allCategories.filter { it.type == CategoryType.INCOME }
    val expenseCategories = allCategories.filter { it.type == CategoryType.EXPENSE }

    // Mendapatkan kategori berdasarkan ID
    fun getCategoryById(id: Int): Category {
        return allCategories.find { it.id == id } ?: Other_Expense
    }
}