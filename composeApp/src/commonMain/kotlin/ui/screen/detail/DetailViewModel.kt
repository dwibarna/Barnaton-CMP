package ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Resource
import data.source.remote.WatchRoomSocketClient
import domain.model.TvDetailSeries
import domain.model.TvFavorite
import domain.model.TvVideo
import domain.model.WatchRoomState
import domain.usecase.TvSeriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val useCase: TvSeriesUseCase,
    private val watchRoomSocketClient: WatchRoomSocketClient
) : ViewModel() {

    private val _uiStateDetail: MutableStateFlow<Resource<TvDetailSeries>> =
        MutableStateFlow(Resource.Loading())
    val uiStateDetail: StateFlow<Resource<TvDetailSeries>> get() = _uiStateDetail

    private val _uiStateVideos: MutableStateFlow<Resource<List<TvVideo>>> =
        MutableStateFlow(Resource.Loading())
    val uiStateVideos: StateFlow<Resource<List<TvVideo>>> get() = _uiStateVideos

    private val _uiTvFavorite: MutableStateFlow<TvFavorite?> =
        MutableStateFlow(null)
    val uiTvFavorite: StateFlow<TvFavorite?> get() = _uiTvFavorite

    private val _stateItemFavorite: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val stateItemFavorite: StateFlow<Boolean> get() = _stateItemFavorite

    private val _watchRoomState: MutableStateFlow<WatchRoomState> =
        MutableStateFlow(WatchRoomState())
    val watchRoomState: StateFlow<WatchRoomState> get() = _watchRoomState

    private var watchRoomJob: Job? = null

    fun getDetailData(id: Int) = viewModelScope.launch {
        useCase.getDetailFavorite(id = id).collect {
            _uiStateDetail.value = it
        }
    }

    fun getTvVideos(id: Int) = viewModelScope.launch {
        useCase.getTvVideos(id = id).collect {
            _uiStateVideos.value = it
        }
    }

    fun insertTvFavorite(entity: TvFavorite) = viewModelScope.launch {
        useCase.insertTvFavorite(entity)
        _stateItemFavorite.value = true
    }

    fun deleteTvFavorite(id: Int) = viewModelScope.launch {
        useCase.deleteTvFavorite(id = id)
        _stateItemFavorite.value = false
    }

    fun getTvFavorite(id: Int) = viewModelScope.launch {
        useCase.getTvFavorite(id = id).collect { favorite ->
            _uiTvFavorite.value = favorite
            _stateItemFavorite.value = favorite.id == id
        }
    }

    fun observeWatchRoom(id: Int) {
        watchRoomJob?.cancel()
        watchRoomJob = viewModelScope.launch {
            watchRoomSocketClient.observeRoom(id).collect { state ->
                _watchRoomState.value = state
            }
        }
    }

    fun leaveWatchRoom() {
        watchRoomJob?.cancel()
        watchRoomJob = null
        _watchRoomState.value = WatchRoomState()
    }
}
