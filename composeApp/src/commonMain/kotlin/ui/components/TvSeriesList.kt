package ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import domain.model.TvFavorite
import domain.model.TvSeries

@Composable
fun TvSeriesList(
    items: List<TvSeries>,
    modifier: Modifier = Modifier,
    stateHome: Boolean = true,
    navigateToDetail: ((Int) -> Unit)? = null
) {
    var stateExpand by remember { mutableStateOf(false) }
    val visibleList = if (stateExpand) items else items.take(3)

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        items(
            items = if (stateHome) visibleList else items,
            key = { it.id }
        ) {
            CardItem(tvSeries = it, navigateToDetail = navigateToDetail)
        }
        if (stateHome) {
            item {
                TextButton(
                    onClick = { stateExpand = !stateExpand },
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Text(text = if (stateExpand) "Show Less" else "Show More", color = Color.White)
                }
            }
        }
    }
}


@Composable
fun TvSeriesListSearch(
    items: List<TvSeries>,
    modifier: Modifier = Modifier,
    navigateToDetail: ((Int) -> Unit)? = null
) {

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp)

    ) {
        items(
            items = items,
            key = { it.id }
        ) {
            CardItem(tvSeries = it, navigateToDetail = navigateToDetail)
        }
    }
}


@Composable
fun TvSeriesFavoriteList(
    items: List<TvFavorite>,
    modifier: Modifier = Modifier,
    navigateToDetail: ((Int) -> Unit)? = null
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(
            items = items,
            key = { it.id }
        ) {
            CardItemFavorite(tvFavorite = it, navigateToDetail = navigateToDetail)
        }
    }
}