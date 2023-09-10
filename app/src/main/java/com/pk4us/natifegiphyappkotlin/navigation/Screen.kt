package com.pk4us.natifegiphyappkotlin.navigation

const val DETAIL_ARGUMENT_KEY = "link"

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object Detail : Screen(route = "detail_screen/{$DETAIL_ARGUMENT_KEY}") {
        fun withArgs(link: String): String {
            return this.route.replace(oldValue = "{$DETAIL_ARGUMENT_KEY}", newValue = link)
        }
    }
}
