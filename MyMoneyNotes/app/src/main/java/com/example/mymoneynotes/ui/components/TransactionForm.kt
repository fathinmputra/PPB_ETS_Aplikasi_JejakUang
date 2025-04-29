package com.example.mymoneynotes.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mymoneynotes.model.Category
import com.example.mymoneynotes.model.TransactionType
import java.util.Date  

@Composable
fun TransactionForm(
    amount: String,
    description: String,
    transactionType: TransactionType,
    selectedCategory: Category?,
    categories: List<Category>,
    selectedDate: Date,
    onAmountChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTransactionTypeChange: (TransactionType) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onDateSelected: (Date) -> Unit,
    onSaveClick: () -> Unit,
    isValid: Boolean = amount.isNotEmpty() && selectedCategory != null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Meningkatkan padding bawah lagi
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Transaction Type Selector
            TransactionTypeSelector(
                selectedType = transactionType,
                onTypeSelected = onTransactionTypeChange
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Amount Input
            AmountInput(
                amount = amount,
                onAmountChange = onAmountChange
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description Input
            DescriptionInput(
                description = description,
                onDescriptionChange = onDescriptionChange
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Selector or Dropdown
            CategoryDropdown(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date Time Picker
            DateTimePicker(
                selectedDate = selectedDate,
                onDateSelected = onDateSelected
            )

            // Tambahkan spacer kecil di bawah
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Save Button fixed at bottom - posisi lebih ke atas
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 8.dp), // Meningkatkan padding vertikal
            enabled = isValid,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Simpan Transaksi")
            }
        }
    }
}