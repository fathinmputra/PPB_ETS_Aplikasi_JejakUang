package com.example.mymoneynotes.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymoneynotes.ui.screens.*
import com.example.mymoneynotes.viewmodel.TransactionViewModel

// Definisi rute navigasi
object NavRoute {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val ADD_TRANSACTION = "add_transaction"
    const val TRANSACTION_LIST = "transaction_list"
    const val CHART = "chart"
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavRoute.SPLASH, 
    modifier: Modifier = Modifier
) {
    // State untuk mengecek apakah splash screen sudah selesai
    var showSplash by remember { mutableStateOf(true) }

    // Membuat ViewModel yang akan digunakan di semua screen
    val transactionViewModel: TransactionViewModel = viewModel()

    // Definisikan navigasi antar screen
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Splash Screen
        composable(NavRoute.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.popBackStack()
                    navController.navigate(NavRoute.HOME)
                    showSplash = false
                }
            )
        }

        // Home Screen
        composable(NavRoute.HOME) {
            HomeScreen(
                viewModel = transactionViewModel,
                onNavigateToAddTransaction = {
                    navController.navigate(NavRoute.ADD_TRANSACTION)
                },
                onNavigateToTransactionList = {
                    navController.navigate(NavRoute.TRANSACTION_LIST)
                },
                onNavigateToChart = {
                    navController.navigate(NavRoute.CHART)
                }
            )
        }

        // Add Transaction Screen
        composable(NavRoute.ADD_TRANSACTION) {
            AddTransactionScreen(
                viewModel = transactionViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Transaction List Screen
        composable(NavRoute.TRANSACTION_LIST) {
            TransactionListScreen(
                viewModel = transactionViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Chart Screen
        composable(NavRoute.CHART) {
            ChartScreen(
                viewModel = transactionViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}