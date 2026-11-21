package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.TvFavorite
import domain.model.TvSeries
import ui.theme.Cerulean
import ui.theme.TextSecondary

@Composable
fun TvSeriesList(
    items: List<TvSeries>,
    modifier: Modifier = Modifier,
    stateHome: Boolean = true,
    navigateToDetail: ((Int) -> Unit)? = null
) {
    var stateExpand by remember { mutableStateOf(false) }
    val visibleList = if (stateHome && !stateExpand) items.take(3) else items

    Column(modifier = modifier.fillMaxWidth()) {
        visibleList.forEach { series ->
            CardItem(tvSeries = series, navigateToDetail = navigateToDetail)
        }
        if (stateHome && items.size > 3) {
            TextButton(
                onClick = { stateExpand = !stateExpand },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text(
                    text = if (stateExpand) "Show Less" else "Show More",
                    color = Cerulean
                )
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
    Column(modifier = modifier.fillMaxWidth()) {
        items.forEach { series ->
            CardItem(tvSeries = series, navigateToDetail = navigateToDetail)
        }
    }
}

@Composable
fun TvSeriesFavoriteList(
    items: List<TvFavorite>,
    modifier: Modifier = Modifier,
    navigateToDetail: ((Int) -> Unit)? = null
) {
    if (items.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No favorite TV series yet",
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(items = items, key = { it.id }) { favorite ->
            CardItemFavorite(tvFavorite = favorite, navigateToDetail = navigateToDetail)
        }
    }
}
