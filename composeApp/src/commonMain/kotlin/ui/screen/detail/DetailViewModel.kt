package ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Resource
import domain.model.TvDetailSeries
import domain.model.TvFavorite
import domain.usecase.TvSeriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val useCase: TvSeriesUseCase
) : ViewModel() {

    private val _uiStateDetail: MutableStateFlow<Resource<TvDetailSeries>> =
        MutableStateFlow(Resource.Loading())

    val uiStateDetail: StateFlow<Resource<TvDetailSeries>> get() = _uiStateDetail

    private val _uiTvFavorite: MutableStateFlow<TvFavorite?> =
        MutableStateFlow(null)

    val uiTvFavorite: StateFlow<TvFavorite?> get() = _uiTvFavorite

    private val _stateItemFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val stateItemFavorite: StateFlow<Boolean> get() = _stateItemFavorite

    fun getStateItemFavorite(boolean: Boolean) = viewModelScope.launch {
        _stateItemFavorite.value = boolean
    }

    fun getDetailData(id: Int) = viewModelScope.launch {
        useCase.getDetailFavorite(id = id).collect {
            _uiStateDetail.value = it
        }
    }

    fun insertTvFavorite(entity: TvFavorite) = viewModelScope.launch {
        useCase.insertTvFavorite(entity)
    }

    fun deleteTvFavorite(id: Int) = viewModelScope.launch {
        useCase.deleteTvFavorite(id = id)
    }

    suspend fun getTvFavorite(id: Int) = viewModelScope.launch {
        useCase.getTvFavorite(id = id).collect {
            _uiTvFavorite.value = it
        }

        /*
        {
        return useCase.getTvFavorite(id = id)
    }
         */
    }
}