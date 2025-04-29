package com.example.mymoneynotes.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.mymoneynotes.ui.components.TransactionForm
import com.example.mymoneynotes.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: TransactionViewModel,
    onNavigateBack: () -> Unit
) {
    val amount by viewModel.transactionAmount.collectAsState()
    val description by viewModel.transactionDescription.collectAsState()
    val transactionType by viewModel.transactionType.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories by viewModel.categoriesStateFlow.collectAsState()
    val selectedDate by viewModel.transactionDate.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Transaksi") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                windowInsets = WindowInsets(0) // Menghilangkan space atas
            )
        },
        contentWindowInsets = WindowInsets(0) // Menghilangkan space di content
    ) { innerPadding ->
        TransactionForm(
            amount = amount,
            description = description,
            transactionType = transactionType,
            selectedCategory = selectedCategory,
            categories = categories,
            selectedDate = selectedDate,
            onAmountChange = viewModel::updateTransactionAmount,
            onDescriptionChange = viewModel::updateTransactionDescription,
            onTransactionTypeChange = viewModel::updateTransactionType,
            onCategorySelected = viewModel::updateSelectedCategory,
            onDateSelected = viewModel::updateTransactionDate,
            onSaveClick = {
                viewModel.addTransaction()
                onNavigateBack()
            },
            isValid = amount.isNotEmpty() && amount.toDoubleOrNull() ?: 0.0 > 0 && selectedCategory != null,
            modifier = Modifier.padding(innerPadding)
        )
    }
}