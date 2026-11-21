package ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.Resource
import domain.model.TvSeries
import org.koin.compose.viewmodel.koinViewModel
import ui.components.Banner
import ui.components.HomeSection
import ui.components.TvSeriesList
import ui.components.TvSeriesListSearch
import ui.theme.DarkBackground
import ui.theme.TextSecondary

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    navigateToDetail: ((Int) -> Unit)? = null
) {
    val stateOnAir by viewModel.uiStateOnAir.collectAsState()
    val statePopular by viewModel.uiStatePopular.collectAsState()
    val stateTopRated by viewModel.uiStateTopRated.collectAsState()
    val stateOnSearch by viewModel.uiStateOnSearch.collectAsState()
    var stateQuery by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize().background(DarkBackground)) {
        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            item {
                Banner(
                    onSearch = { query ->
                        stateQuery = query
                        if (query.isNotBlank()) viewModel.getAllOnSearch(query)
                    }
                )
            }
            if (stateQuery.isBlank()) {
                item { SeriesSection("On Air", stateOnAir, navigateToDetail) }
                item { SeriesSection("Popular", statePopular, navigateToDetail) }
                item { SeriesSection("Top Rated", stateTopRated, navigateToDetail) }
            } else {
                item { SearchSection(stateOnSearch, navigateToDetail) }
            }
        }
    }
}

@Composable
private fun SeriesSection(
    title: String,
    state: Resource<List<TvSeries>>,
    navigateToDetail: ((Int) -> Unit)?
) {
    HomeSection(title = title) {
        when (state) {
            is Resource.Error -> SectionError(state.message)
            is Resource.Loading -> SectionLoading()
            is Resource.Success -> TvSeriesList(
                items = state.data.orEmpty(),
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
private fun SearchSection(
    state: Resource<List<TvSeries>>,
    navigateToDetail: ((Int) -> Unit)?
) {
    HomeSection(title = "Search TV Series") {
        when (state) {
            is Resource.Error -> SectionError(state.message)
            is Resource.Loading -> SectionLoading()
            is Resource.Success -> TvSeriesListSearch(
                items = state.data.orEmpty(),
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
private fun SectionLoading() {
    Box(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SectionError(message: String?) {
    Text(
        text = message ?: "Unknown error",
        color = TextSecondary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}
