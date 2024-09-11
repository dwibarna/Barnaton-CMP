package data.source.local

import data.source.local.entity.TvFavoriteEntity
import data.source.local.entity.TvSeriesEntity
import data.source.local.room.TvSeriesDatabase
import kotlinx.coroutines.flow.Flow

class LocalDataSource (private val database: TvSeriesDatabase) {

    fun getAllTvSeries(typeEntity: String): Flow<List<TvSeriesEntity>> =
        database.tvSeriesDao().getAllTvSeries(typeEntity = typeEntity)

    suspend fun insertAllTvSeries(entity: List<TvSeriesEntity>) =
        database.tvSeriesDao().insertAllTvSeries(entity)

    suspend fun deleteAllTvSeries(typeEntity: String) =
        database.tvSeriesDao().deleteAllTvSeries(typeEntity)


    fun getAllTvFavorite(): Flow<List<TvFavoriteEntity>> =
        database.tvFavoriteDao().getAllTvFavorite()

    suspend fun insertTvFavorite(entity: TvFavoriteEntity) =
        database.tvFavoriteDao().insertTvFavorite(entity = entity)

    suspend fun deleteTvFavorite(id: Int) = database.tvFavoriteDao().deleteTvFavorite(id = id)

    suspend fun getTvFavorite(id: Int): TvFavoriteEntity? = database.tvFavoriteDao().getTvFavorite(id = id)
}