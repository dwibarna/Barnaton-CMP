package data.repository

import data.NetworkBoundResource
import data.Resource
import data.source.local.LocalDataSource
import data.source.local.entity.TypeEntity
import data.source.remote.RemoteDataSource
import data.source.remote.response.TvSeriesResponse
import domain.model.TvDetailSeries
import domain.model.TvFavorite
import domain.model.TvSeries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import utils.DataMapper
import utils.NetworkError
import utils.Result

interface TvSeriesRepository {
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

class TvSeriesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : TvSeriesRepository {
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

    override fun getDetailFavorite(id: Int): Flow<Resource<TvDetailSeries>> {
        return flow {
            emit(Resource.Loading())
            flow { emit(remoteDataSource.getDetailTvSeries(id)) }.let {
                when (val apiResponse = it.first()) {
                    is Result.Error -> emit(Resource.Error(message = apiResponse.error.toString()))
                    is Result.Success -> emit(Resource.Success(DataMapper.tvDetailResponseToDomain(apiResponse.data)))
                }
            }

        }
    }

    override fun getAllTvFavorite(): Flow<Resource<List<TvFavorite>>> {
        return flow {
            emit(Resource.Loading())
            emitAll(
                localDataSource.getAllTvFavorite().map {
                    Resource.Success(DataMapper.tvFavoriteListEntityToDomain(it))
                }
            )
        }
    }

    override suspend fun insertTvFavorite(entity: TvFavorite) {
        localDataSource.insertTvFavorite(DataMapper.tvFavoriteDomainToEntity(entity))
    }

    override suspend fun deleteTvFavorite(id: Int) {
        localDataSource.deleteTvFavorite(id)
    }

    override suspend fun getTvFavorite(id: Int): Flow<TvFavorite> {
        return flow { DataMapper.tvFavoriteEntityToDomain(localDataSource.getTvFavorite(id)) }
    }

    override suspend fun getSearchTvSeries(query: String): Flow<Resource<List<TvSeries>>> {
        return object : NetworkBoundResource<List<TvSeries>, TvSeriesResponse>() {
            override fun loadFromDB(): Flow<List<TvSeries>> {
                return localDataSource.getAllTvSeries(typeEntity = TypeEntity.OTHER.value)
                    .map(DataMapper::tvEntityToDomain)
            }

            override suspend fun createCall(): Flow<Result<TvSeriesResponse, NetworkError>> {
                return remoteDataSource.getSearchTvSeries(query = query)
            }

            override suspend fun saveCallResult(data: TvSeriesResponse) {
                localDataSource.deleteAllTvSeries(TypeEntity.OTHER.value)
                DataMapper.tvResponseToEntity(data, type = TypeEntity.OTHER.value).let {
                    localDataSource.insertAllTvSeries(it)
                }
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
