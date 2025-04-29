package com.example.mymoneynotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mymoneynotes.ui.components.BottomNavigation
import com.example.mymoneynotes.ui.navigation.NavGraph
import com.example.mymoneynotes.ui.navigation.NavRoute
import com.example.mymoneynotes.ui.screens.SplashScreen
import com.example.mymoneynotes.ui.theme.MyMoneyNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var showSplash by remember { mutableStateOf(true) }

            MyMoneyNotesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showSplash) {
                        SplashScreen(
                            onSplashFinished = {
                                showSplash = false
                            }
                        )
                    } else {
                        MyMoneyApp()
                    }
                }
            }
        }
    }
}

@Composable
fun MyMoneyApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            startDestination = NavRoute.HOME,
            modifier = Modifier.padding(innerPadding)
        )
    }
}