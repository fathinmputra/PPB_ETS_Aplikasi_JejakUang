package com.example.mymoneynotes.data

import com.example.mymoneynotes.model.Categories
import com.example.mymoneynotes.model.Transaction
import com.example.mymoneynotes.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

/**
 * Repository untuk mengelola data transaksi
 * Karena ini hanya frontend, data disimpan di memori selama aplikasi berjalan
 */
class TransactionRepository {
    // Data transaksi yang disimpan dalam StateFlow untuk reaktivitas UI
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    // Menambahkan data sample saat inisialisasi
    init {
//        addSampleTransactions()
    }

    // Menambahkan transaksi baru
    fun addTransaction(transaction: Transaction) {
        _transactions.update { currentList ->
            currentList + transaction
        }
    }

    // Mengupdate transaksi yang sudah ada
    fun updateTransaction(updatedTransaction: Transaction) {
        _transactions.update { currentList ->
            currentList.map {
                if (it.id == updatedTransaction.id) updatedTransaction else it
            }
        }
    }

    // Menghapus transaksi
    fun deleteTransaction(transactionId: String) {
        _transactions.update { currentList ->
            currentList.filter { it.id != transactionId }
        }
    }

    // Mendapatkan transaksi berdasarkan ID
    fun getTransactionById(id: String): Transaction? {
        return transactions.value.find { it.id == id }
    }

    // Mendapatkan total pemasukan
    fun getTotalIncome(): Double {
        return transactions.value
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
    }

    // Mendapatkan total pengeluaran
    fun getTotalExpense(): Double {
        return transactions.value
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
    }

    // Mendapatkan saldo (pemasukan - pengeluaran)
    fun getBalance(): Double {
        return getTotalIncome() - getTotalExpense()
    }

    // Data transaksi sample untuk testing
    private fun addSampleTransactions() {
        val sampleTransactions = listOf(
            Transaction(
                amount = 3000000.0,
                description = "Gaji bulanan",
                category = Categories.Salary,
                type = TransactionType.INCOME,
                date = Date()
            ),
            Transaction(
                amount = 500000.0,
                description = "Bonus proyek",
                category = Categories.Salary,
                type = TransactionType.INCOME,
                date = Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000) // 2 hari lalu
            ),
            Transaction(
                amount = 200000.0,
                description = "Makan di restoran",
                category = Categories.Food,
                type = TransactionType.EXPENSE,
                date = Date()
            ),
            Transaction(
                amount = 150000.0,
                description = "Bensin",
                category = Categories.Transport,
                type = TransactionType.EXPENSE,
                date = Date(System.currentTimeMillis() - 1 * 24 * 60 * 60 * 1000) // 1 hari lalu
            ),
            Transaction(
                amount = 350000.0,
                description = "Belanja bulanan",
                category = Categories.Shopping,
                type = TransactionType.EXPENSE,
                date = Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000) // 3 hari lalu
            ),
            Transaction(
                amount = 100000.0,
                description = "Nonton bioskop",
                category = Categories.Entertainment,
                type = TransactionType.EXPENSE,
                date = Date(System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000) // 5 hari lalu
            )
        )

        _transactions.value = sampleTransactions
    }
}