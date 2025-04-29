package com.example.mymoneynotes.model

import java.util.Date
import java.util.UUID

enum class TransactionType {
    INCOME, EXPENSE
}

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val description: String,
    val category: Category,
    val type: TransactionType,
    val date: Date = Date(), // Menggunakan java.util.Date yang kompatibel dengan API level lebih rendah
    val isRecurring: Boolean = false
) {
    val isIncome: Boolean
        get() = type == TransactionType.INCOME

    val isExpense: Boolean
        get() = type == TransactionType.EXPENSE

    // Untuk menampilkan jumlah dengan tanda +/- berdasarkan tipe transaksi
    val formattedAmount: String
        get() {
            val prefix = if (isIncome) "+" else "-"
            return "$prefix Rp${amount}"
        }
}