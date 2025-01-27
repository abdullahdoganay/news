package com.abd.news.navigation

sealed class NavigationItem(val route: String) {
    object Home : NavigationItem("home")
    object Detail : NavigationItem("detail")
}
