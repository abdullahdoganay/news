package com.abd.news

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.abd.news.navigation.AppNavHost
import com.abd.news.navigation.NavigationItem
import com.abd.news.ui.theme.NewsTheme
import com.abd.news.viewmodel.HomeScreenViewModel
class MainActivity : ComponentActivity() {

    private val viewModel: HomeScreenViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NewsTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigation(backgroundColor = Color.White) {
                            BottomNavigationItem(
                                selected = true,
                                onClick = { navController.navigate(NavigationItem.Home.route) },
                                icon = {},
                                label = { Text("Home") }
                            )
                        }
                    }
                ) {
                    AppNavHost(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}
