package domain.usecase

import data.Resource
import data.repository.TvSeriesRepository
import domain.model.TvSeries
import kotlinx.coroutines.flow.Flow

interface TvSeriesUseCase {
    fun getAllTopRated(): Flow<Resource<List<TvSeries>>>

    fun getAllTopOnAir(): Flow<Resource<List<TvSeries>>>

    fun getAllPopular(): Flow<Resource<List<TvSeries>>>
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

}