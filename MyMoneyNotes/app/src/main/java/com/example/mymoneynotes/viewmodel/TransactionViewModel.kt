package com.example.mymoneynotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymoneynotes.data.TransactionRepository
import com.example.mymoneynotes.model.Categories
import com.example.mymoneynotes.model.Category
import com.example.mymoneynotes.model.CategoryType
import com.example.mymoneynotes.model.Transaction
import com.example.mymoneynotes.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class TransactionViewModel : ViewModel() {

    // Repository untuk mengakses data
    private val repository = TransactionRepository()

    // State untuk semua transaksi
    val transactions = repository.transactions

    // State untuk filter tipe transaksi
    private val _transactionTypeFilter = MutableStateFlow<TransactionType?>(null)
    val transactionTypeFilter = _transactionTypeFilter.asStateFlow()

    // State untuk filter kategori
    private val _categoryFilter = MutableStateFlow<Category?>(null)
    val categoryFilter = _categoryFilter.asStateFlow()

    // State untuk transaksi yang difilter
    val filteredTransactions = combine(
        transactions,
        transactionTypeFilter,
        categoryFilter
    ) { allTransactions, typeFilter, catFilter ->
        allTransactions.filter { transaction ->
            (typeFilter == null || transaction.type == typeFilter) &&
                    (catFilter == null || transaction.category.id == catFilter.id)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // State untuk form input transaksi baru
    private val _transactionAmount = MutableStateFlow("")
    val transactionAmount = _transactionAmount.asStateFlow()

    private val _transactionDescription = MutableStateFlow("")
    val transactionDescription = _transactionDescription.asStateFlow()

    private val _transactionType = MutableStateFlow(TransactionType.EXPENSE)
    val transactionType = _transactionType.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    // State flow untuk daftar kategori yang tersedia sesuai jenis transaksi
    private val _categoriesStateFlow = MutableStateFlow<List<Category>>(Categories.expenseCategories)
    val categoriesStateFlow = _categoriesStateFlow.asStateFlow()

    // Inisialisasi
    init {
        // Set kategori default saat aplikasi dimulai
        _categoriesStateFlow.value = Categories.expenseCategories
    }

    // Statistik keuangan
    val totalIncome: StateFlow<Double> = repository.transactions.combine(MutableStateFlow(0)) { transactions, _ ->
        transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpense: StateFlow<Double> = repository.transactions.combine(MutableStateFlow(0)) { transactions, _ ->
        transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val balance: StateFlow<Double> = combine(totalIncome, totalExpense) { income, expense ->
        income - expense
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // Metode untuk mengatur filter tipe transaksi
    fun setTransactionTypeFilter(type: TransactionType?) {
        _transactionTypeFilter.value = type
    }

    // Metode untuk mengatur filter kategori
    fun setCategoryFilter(category: Category?) {
        _categoryFilter.value = category
    }

    // Metode untuk mengubah nilai input transaksi
    fun updateTransactionAmount(amount: String) {
        _transactionAmount.value = amount
    }

    fun updateTransactionDescription(description: String) {
        _transactionDescription.value = description
    }

    // Metode untuk mengubah tipe transaksi dengan respons cepat
    fun updateTransactionType(type: TransactionType) {
        _transactionType.value = type
        // Reset kategori saat mengubah tipe transaksi
        _selectedCategory.value = null

        // Perbarui kategori yang tersedia berdasarkan tipe transaksi
        viewModelScope.launch {
            _categoriesStateFlow.value = if (type == TransactionType.INCOME) {
                Categories.incomeCategories
            } else {
                Categories.expenseCategories
            }
        }
    }

    fun updateSelectedCategory(category: Category) {
        _selectedCategory.value = category
    }

    // Metode untuk mendapatkan kategori berdasarkan tipe transaksi
    fun getCategoriesByType(): List<Category> {
        return _categoriesStateFlow.value
    }

    // Metode untuk menambah transaksi baru
    fun addTransaction() {
        val amountValue = _transactionAmount.value.toDoubleOrNull() ?: 0.0
        val description = _transactionDescription.value
        val category = _selectedCategory.value ?:
        if (_transactionType.value == TransactionType.INCOME) Categories.Other_Income
        else Categories.Other_Expense

        if (amountValue <= 0) return

        val newTransaction = Transaction(
            amount = amountValue,
            description = description,
            category = category,
            type = _transactionType.value,
            date = _transactionDate.value // Gunakan tanggal yang dipilih
        )

        viewModelScope.launch {
            repository.addTransaction(newTransaction)
            resetForm()
        }
    }

    // Metode untuk mengupdate transaksi
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    // Metode untuk menghapus transaksi
    fun deleteTransaction(transactionId: String) {
        viewModelScope.launch {
            repository.deleteTransaction(transactionId)
        }
    }

    // Metode untuk mendapatkan data transaksi untuk grafik
    fun getCategoryExpensesData(): Map<Category, Double> {
        val expensesByCategory = mutableMapOf<Category, Double>()

        transactions.value
            .filter { it.type == TransactionType.EXPENSE }
            .forEach { transaction ->
                val currentTotal = expensesByCategory[transaction.category] ?: 0.0
                expensesByCategory[transaction.category] = currentTotal + transaction.amount
            }

        return expensesByCategory
    }

    // Metode untuk mendapatkan data pemasukan untuk grafik
    fun getCategoryIncomesData(): Map<Category, Double> {
        val incomesByCategory = mutableMapOf<Category, Double>()

        transactions.value
            .filter { it.type == TransactionType.INCOME }
            .forEach { transaction ->
                val currentTotal = incomesByCategory[transaction.category] ?: 0.0
                incomesByCategory[transaction.category] = currentTotal + transaction.amount
            }

        return incomesByCategory
    }

    // Reset form input setelah menambah transaksi
    private fun resetForm() {
        _transactionAmount.value = ""
        _transactionDescription.value = ""
        _selectedCategory.value = null
        _transactionDate.value = Date() // Reset ke tanggal/waktu saat ini
    }

    private val _transactionDate = MutableStateFlow(Date())
    val transactionDate = _transactionDate.asStateFlow()

    // Fungsi untuk update tanggal
    fun updateTransactionDate(date: Date) {
        _transactionDate.value = date
    }

    enum class SortOrder {
        NEWEST_FIRST, OLDEST_FIRST
    }

    // State untuk urutan sortir
    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST_FIRST)
    val sortOrder = _sortOrder.asStateFlow()

    // Transaksi yang sudah difilter dan diurutkan
    val filteredAndSortedTransactions = combine(
        transactions,
        transactionTypeFilter,
        categoryFilter,
        sortOrder
    ) { allTransactions, typeFilter, catFilter, sort ->
        val filtered = allTransactions.filter { transaction ->
            (typeFilter == null || transaction.type == typeFilter) &&
                    (catFilter == null || transaction.category.id == catFilter.id)
        }

        when (sort) {
            SortOrder.NEWEST_FIRST -> filtered.sortedByDescending { it.date }
            SortOrder.OLDEST_FIRST -> filtered.sortedBy { it.date }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // Fungsi untuk mengganti urutan sortir
    fun toggleSortOrder() {
        _sortOrder.value = if (_sortOrder.value == SortOrder.NEWEST_FIRST) {
            SortOrder.OLDEST_FIRST
        } else {
            SortOrder.NEWEST_FIRST
        }
    }
}