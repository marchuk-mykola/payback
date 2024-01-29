package com.payback.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.payback.search.SearchScreen

const val SEARCH_ROUTE = "search_route"

fun NavGraphBuilder.searchScreen(
    onNavigateToSearch: (Int) -> Unit
) {
    composable(SEARCH_ROUTE) {
        SearchScreen(navigateToDetails = { onNavigateToSearch(it) })
    }
}


