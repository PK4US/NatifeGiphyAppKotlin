package com.pk4us.natifegiphyappkotlin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pk4us.natifegiphyappkotlin.activity.DetailScreen
import com.pk4us.natifegiphyappkotlin.activity.HomeScreen

@Composable
fun SetupNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument(DETAIL_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) { entry ->
            DetailScreen(gifUrl = entry.arguments?.getString(DETAIL_ARGUMENT_KEY))
        }
    }
}