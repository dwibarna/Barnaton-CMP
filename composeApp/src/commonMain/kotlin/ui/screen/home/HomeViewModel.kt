package ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Resource
import domain.model.TvSeries
import domain.usecase.TvSeriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val useCase: TvSeriesUseCase
) : ViewModel() {

    private val _uiStatePopular: MutableStateFlow<Resource<List<TvSeries>>> =
        MutableStateFlow(Resource.Loading())
    val uiStatePopular: StateFlow<Resource<List<TvSeries>>>
        get() = _uiStatePopular


    private val _uiStateTopRated: MutableStateFlow<Resource<List<TvSeries>>> =
        MutableStateFlow(Resource.Loading())
    val uiStateTopRated: StateFlow<Resource<List<TvSeries>>>
        get() = _uiStateTopRated


    private val _uiStateOnAir: MutableStateFlow<Resource<List<TvSeries>>> =
        MutableStateFlow(Resource.Loading())
    val uiStateOnAir: StateFlow<Resource<List<TvSeries>>>
        get() = _uiStateOnAir

    private val _uiStateOnSearch: MutableStateFlow<Resource<List<TvSeries>>> =
        MutableStateFlow(Resource.Loading())
    val uiStateOnSearch: StateFlow<Resource<List<TvSeries>>>
        get() = _uiStateOnSearch

    fun getAllPopular() = viewModelScope.launch {
        useCase.getAllPopular().collect {
            _uiStatePopular.value = it
        }
    }

    fun getAllTopRated() = viewModelScope.launch {
        useCase.getAllTopRated().collect {
            _uiStateTopRated.value = it
        }
    }

    fun getAllOnAir() = viewModelScope.launch {
        useCase.getAllTopOnAir().collect {
            _uiStateOnAir.value = it
        }
    }

    fun getAllOnSearch(query: String) = viewModelScope.launch(Dispatchers.IO) {
        useCase.getSearchTvSeries(query).collect {
            _uiStateOnSearch.value = it
        }
    }
}