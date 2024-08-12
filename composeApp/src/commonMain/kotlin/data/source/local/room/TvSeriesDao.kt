package data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import data.source.local.entity.TvSeriesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TvSeriesDao {
    @Query("SELECT * FROM tv_series WHERE type_entity = :typeEntity")
    fun getAllTvSeries(typeEntity: String): Flow<List<TvSeriesEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertAllTvSeries(entity: List<TvSeriesEntity>)

    @Query("DELETE from tv_series where type_entity = :typeEntity")
    suspend fun deleteAllTvSeries(typeEntity: String)

}