package data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import data.source.local.entity.TvFavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TvSeriesFavoriteDao {

    @Query("SELECT * FROM tv_favorite")
    fun getAllTvFavorite(): Flow<List<TvFavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvFavorite(entity: TvFavoriteEntity)

    @Query("DELETE from tv_favorite where id = :id")
    suspend fun deleteTvFavorite(id: Int)

    @Query("SELECT * FROM tv_favorite where id = :id")
    suspend fun getTvFavorite(id: Int): TvFavoriteEntity?
}