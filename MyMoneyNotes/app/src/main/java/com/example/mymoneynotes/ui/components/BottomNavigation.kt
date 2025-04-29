package com.example.mymoneynotes.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mymoneynotes.ui.navigation.NavRoute

// Item untuk bottom navigation
sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(
        route = NavRoute.HOME,
        icon = Icons.Default.Home,
        label = "Beranda"
    )

    object Transactions : BottomNavItem(
        route = NavRoute.TRANSACTION_LIST,
        icon = Icons.Default.List,
        label = "Transaksi"
    )

    object AddTransaction : BottomNavItem(
        route = NavRoute.ADD_TRANSACTION,
        icon = Icons.Default.Add,
        label = "Tambah"
    )

    object Chart : BottomNavItem(
        route = NavRoute.CHART,
        icon = Icons.Default.PieChart,
        label = "Grafik"
    )
}

@Composable
fun BottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Definisikan warna kustom
    val bottomNavBackground = Color(0xFFE3F2FD) // Biru sangat muda
    val selectedIndicator = Color(0xFFBBDEFB)   // Biru muda (lebih tua dari background)
    val selectedItemColor = Color(0xFF1976D2)   // Biru untuk ikon dan teks yang dipilih

    // Daftar item untuk bottom navigation
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Transactions,
        BottomNavItem.AddTransaction,
        BottomNavItem.Chart
    )

    // Mendapatkan rute saat ini
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = modifier,
        tonalElevation = 8.dp,
        containerColor = bottomNavBackground  // Warna background kustom
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        // Navigasi ke rute yang dipilih
                        navController.navigate(item.route) {
                            // Pop up to the start destination to avoid building up a stack
                            popUpTo(NavRoute.HOME) {
                                saveState = true
                            }
                            // Hindari navigasi duplikat ke destinasi yang sama
                            launchSingleTop = true
                            // Simpan state navigasi jika navigasi dari navBar
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedItemColor,       // Warna ikon saat dipilih
                    selectedTextColor = selectedItemColor,       // Warna teks saat dipilih
                    indicatorColor = selectedIndicator,          // Warna indikator saat dipilih
                    unselectedIconColor = Color.Gray,            // Warna ikon saat tidak dipilih
                    unselectedTextColor = Color.Gray             // Warna teks saat tidak dipilih
                )
            )
        }
    }
}