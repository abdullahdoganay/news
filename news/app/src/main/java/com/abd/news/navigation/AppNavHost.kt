package com.abd.news.navigation

import com.abd.news.view.ArticleDetailScreen
import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.abd.news.model.Article
import com.abd.news.view.HomeScreen
import com.abd.news.viewmodel.HomeScreenViewModel


@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: HomeScreenViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Home.route
    ) {
        composable(NavigationItem.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onArticleClick = { article ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
                    navController.navigate(NavigationItem.Detail.route)
                }
            )
        }
        composable(
            route = NavigationItem.Detail.route
        ) {
            val article =
                navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
            ArticleDetailScreen(
                article = article,
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}
