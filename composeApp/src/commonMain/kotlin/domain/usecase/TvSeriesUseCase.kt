package domain.usecase

import data.Resource
import data.repository.TvSeriesRepository
import domain.model.TvDetailSeries
import domain.model.TvFavorite
import domain.model.TvSeries
import kotlinx.coroutines.flow.Flow

interface TvSeriesUseCase {
    fun getAllTopRated(): Flow<Resource<List<TvSeries>>>

    fun getAllTopOnAir(): Flow<Resource<List<TvSeries>>>

    fun getAllPopular(): Flow<Resource<List<TvSeries>>>

    fun getDetailFavorite(id: Int): Flow<Resource<TvDetailSeries>>

    fun getAllTvFavorite(): Flow<Resource<List<TvFavorite>>>

    suspend fun insertTvFavorite(entity: TvFavorite)

    suspend fun deleteTvFavorite(id: Int)

    suspend fun getTvFavorite(id: Int): Flow<TvFavorite>

    suspend fun getSearchTvSeries(query: String): Flow<Resource<List<TvSeries>>>
}

class TvSeriesUseCaseImpl(private val repository: TvSeriesRepository): TvSeriesUseCase {

    override fun getAllTopRated(): Flow<Resource<List<TvSeries>>> {
        return repository.getAllTopRated()
    }

    override fun getAllTopOnAir(): Flow<Resource<List<TvSeries>>> {
        return repository.getAllTopOnAir()
    }

    override fun getAllPopular(): Flow<Resource<List<TvSeries>>> {
        return repository.getAllPopular()
    }


    override fun getDetailFavorite(id: Int): Flow<Resource<TvDetailSeries>> {
        return repository.getDetailFavorite(id = id)
    }

    override fun getAllTvFavorite(): Flow<Resource<List<TvFavorite>>> {
        return repository.getAllTvFavorite()
    }

    override suspend fun insertTvFavorite(entity: TvFavorite) {
        return repository.insertTvFavorite(entity)
    }

    override suspend fun deleteTvFavorite(id: Int) {
        return repository.deleteTvFavorite(id = id)
    }

    override suspend fun getTvFavorite(id: Int): Flow<TvFavorite> {
        return repository.getTvFavorite(id = id)
    }

    override suspend fun getSearchTvSeries(query: String): Flow<Resource<List<TvSeries>>> {
        return repository.getSearchTvSeries(query = query)
    }
}