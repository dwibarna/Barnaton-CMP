import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ui.components.BottomBar
import ui.navigations.Screen
import ui.screen.detail.DetailScreen
import ui.screen.favorite.FavoriteScreen
import ui.screen.home.HomeScreen

@Composable
fun MainApp(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DetailScreen.route) {
                BottomBar(
                    navHostController
                )
            }
        },
        modifier = modifier
    ) { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = { id ->
                        navHostController.navigate(Screen.DetailScreen.createRoute(id))
                    }
                )
            }
            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    navigateToDetail = { id ->
                        navHostController.navigate(Screen.DetailScreen.createRoute(id))
                    }
                )
            }
            composable(
                route = Screen.DetailScreen.route,
                arguments = listOf(navArgument("detailId") {
                    type = NavType.IntType
                }),
            ) {
                val id = it.arguments?.getInt("detailId") ?: 0
                DetailScreen(
                    id = id,
                )
            }
        }
    }
}