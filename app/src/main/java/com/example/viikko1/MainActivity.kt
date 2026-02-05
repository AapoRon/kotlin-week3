package com.example.viikko1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.viikko1.ui.theme.Viikko1Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Viikko1Theme {
                val navController = rememberNavController()

                // ✅ Sama ViewModel jaetaan kaikille ruuduille
                val taskViewModel: TaskViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = ROUTE_HOME
                ) {
                    composable(ROUTE_HOME) {
                        HomeScreen(navController, taskViewModel)
                    }
                    composable(ROUTE_CALENDAR) {
                        CalendarScreen(navController, taskViewModel)
                    }
                    composable(ROUTE_SETTINGS) {
                        SettingsScreen(navController)
                    }
                }
            }
        }
    }
}
