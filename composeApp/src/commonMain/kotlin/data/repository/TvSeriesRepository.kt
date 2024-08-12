package data.repository

import data.NetworkBoundResource
import data.Resource
import data.source.local.LocalDataSource
import data.source.local.entity.TypeEntity
import data.source.remote.RemoteDataSource
import data.source.remote.response.TvSeriesResponse
import domain.model.TvSeries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import utils.DataMapper
import utils.NetworkError
import utils.Result

interface TvSeriesRepository {
    fun getAllTopRated(): Flow<Resource<List<TvSeries>>>

    fun getAllTopOnAir(): Flow<Resource<List<TvSeries>>>

    fun getAllPopular(): Flow<Resource<List<TvSeries>>>

/*    fun getDetailFavorite(id: Int): Flow<Resource<TvDetailSeries>>

    fun getAllTvFavorite(): Flow<Resource<List<TvFavorite>>>

    suspend fun insertTvFavorite(entity: TvFavorite)

    suspend fun deleteTvFavorite(id: Int)

    fun getTvFavorite(id: Int): TvFavorite

    suspend fun getSearchTvSeries(query: String): Flow<Resource<List<TvSeries>>>*/
}

class  TvSeriesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
): TvSeriesRepository {
    override fun getAllTopRated(): Flow<Resource<List<TvSeries>>> {
        return object : NetworkBoundResource<List<TvSeries>, TvSeriesResponse>() {
            override fun loadFromDB(): Flow<List<TvSeries>> {
                return localDataSource.getAllTvSeries(
                    typeEntity = TypeEntity.TOP_RATED.value
                ).map {
                    DataMapper.tvEntityToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<Result<TvSeriesResponse, NetworkError>> {
                return flow { emit(remoteDataSource.getTopRated()) }
            }

            override suspend fun saveCallResult(data: TvSeriesResponse) {
                localDataSource.insertAllTvSeries(
                    entity = DataMapper.tvResponseToEntity(data, type = TypeEntity.TOP_RATED.value)
                )
            }

        }.asFlow()
    }

    override fun getAllPopular(): Flow<Resource<List<TvSeries>>> {
        return object : NetworkBoundResource<List<TvSeries>, TvSeriesResponse>() {
            override fun loadFromDB(): Flow<List<TvSeries>> {
                return localDataSource.getAllTvSeries(
                    typeEntity = TypeEntity.POPULAR.value
                ).map {
                    DataMapper.tvEntityToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<Result<TvSeriesResponse, NetworkError>> {
                return flow { emit(remoteDataSource.getPopular()) }
            }

            override suspend fun saveCallResult(data: TvSeriesResponse) {
                localDataSource.insertAllTvSeries(
                    entity = DataMapper.tvResponseToEntity(data, type = TypeEntity.POPULAR.value)
                )
            }

        }.asFlow()
    }

    override fun getAllTopOnAir(): Flow<Resource<List<TvSeries>>> {
        return object : NetworkBoundResource<List<TvSeries>, TvSeriesResponse>() {
            override fun loadFromDB(): Flow<List<TvSeries>> {
                return localDataSource.getAllTvSeries(
                    typeEntity = TypeEntity.ON_AIR.value
                ).map {
                    DataMapper.tvEntityToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<Result<TvSeriesResponse, NetworkError>> {
                return flow { emit(remoteDataSource.getOnTheAir()) }
            }

            override suspend fun saveCallResult(data: TvSeriesResponse) {
                localDataSource.insertAllTvSeries(
                    entity = DataMapper.tvResponseToEntity(data, type = TypeEntity.ON_AIR.value)
                )
            }

        }.asFlow()
    }
}
