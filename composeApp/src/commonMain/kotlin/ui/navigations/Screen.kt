package ui.navigations

sealed class Screen(val route: String) {
    data object Home : Screen("home")

    data object Favorite : Screen("favorite")

    data object DetailScreen : Screen("home/{detailId}") {
        fun createRoute(detailId: Int) = "home/$detailId"
    }
}