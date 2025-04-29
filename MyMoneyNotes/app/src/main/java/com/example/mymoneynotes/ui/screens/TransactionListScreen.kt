package com.example.mymoneynotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mymoneynotes.model.TransactionType
import com.example.mymoneynotes.ui.components.TransactionItem
import com.example.mymoneynotes.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    viewModel: TransactionViewModel,
    onNavigateBack: () -> Unit
) {
    val transactions by viewModel.filteredAndSortedTransactions.collectAsState()
    val typeFilter by viewModel.transactionTypeFilter.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    var showFilterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Transaksi") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    // Sort button
                    IconButton(onClick = { viewModel.toggleSortOrder() }) {
                        Icon(
                            imageVector = if (sortOrder == TransactionViewModel.SortOrder.NEWEST_FIRST)
                                Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                            contentDescription = "Ubah Urutan"
                        )
                    }

                    // Filter button
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                },
                windowInsets = WindowInsets(0)
            )
        },
        contentWindowInsets = WindowInsets(0) // Menghilangkan padding default content
    ) { innerPadding ->
        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada transaksi",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(bottom = 0.dp) // Memastikan tidak ada padding tambahan di bawah
            ) {
                // Display sorting info dengan padding minimal
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp), // Kurangi padding vertikal
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Urutan: ${if (sortOrder == TransactionViewModel.SortOrder.NEWEST_FIRST) "Terbaru dahulu" else "Terlama dahulu"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp) // Kurangi padding vertikal
                ) {
                    items(transactions, key = { it.id }) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onItemClick = { /* Detail view if needed */ },
                            onDeleteClick = { viewModel.deleteTransaction(it) }
                        )
                    }
                }
            }
        }
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Filter Transaksi") },
            text = {
                Column {
                    Text("Pilih jenis transaksi:")

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = typeFilter == null,
                            onClick = { viewModel.setTransactionTypeFilter(null) }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Semua")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = typeFilter == TransactionType.INCOME,
                            onClick = { viewModel.setTransactionTypeFilter(TransactionType.INCOME) }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Pemasukan")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = typeFilter == TransactionType.EXPENSE,
                            onClick = { viewModel.setTransactionTypeFilter(TransactionType.EXPENSE) }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Pengeluaran")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFilterDialog = false }
                ) {
                    Text("Tutup")
                }
            }
        )
    }
}