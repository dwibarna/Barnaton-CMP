package ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Resource
import domain.model.TvFavorite
import domain.usecase.TvSeriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel (
    private val useCase: TvSeriesUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<Resource<List<TvFavorite>>> =
        MutableStateFlow(Resource.Loading())
    val uiState: StateFlow<Resource<List<TvFavorite>>>
        get() = _uiState

    fun getAllTvFavorite() = viewModelScope.launch {
        useCase.getAllTvFavorite().collect {
            _uiState.value = it
        }
    }
}