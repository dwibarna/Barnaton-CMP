package ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ui.navigations.NavigationItem
import ui.navigations.Screen
import ui.theme.CardSurface
import ui.theme.Cerulean
import ui.theme.DarkSurface
import ui.theme.TextSecondary

@Composable
fun BottomBar(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navigationItems = listOf(
        NavigationItem(title = "Home", icon = Icons.Default.Home, screen = Screen.Home),
        NavigationItem(title = "Favorite", icon = Icons.Default.Favorite, screen = Screen.Favorite)
    )

    NavigationBar(modifier = modifier, containerColor = DarkSurface) {
        navigationItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = { navigateToTab(navHostController, item.screen.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Cerulean,
                    selectedTextColor = Cerulean,
                    indicatorColor = CardSurface,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }
    }
}

private fun navigateToTab(navHostController: NavHostController, route: String) {
    navHostController.navigate(route) {
        popUpTo(navHostController.graph.findStartDestination().route ?: Screen.Home.route) {
            saveState = true
        }
        restoreState = true
        launchSingleTop = true
    }
}
