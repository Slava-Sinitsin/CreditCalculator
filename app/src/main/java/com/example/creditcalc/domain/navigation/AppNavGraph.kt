package com.example.creditcalc.domain.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.creditcalc.ui.screens.MainScreen
import com.example.creditcalc.ui.theme.CreditCalcTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppNavGraph : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreditCalcTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    route = Graph.ROOT,
                    startDestination = Graph.MAIN_SCREEN
                ) {
                    composable(route = Graph.MAIN_SCREEN) {
                        MainScreen()
                    }
                }
            }
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val MAIN_SCREEN = "main_screen"
}