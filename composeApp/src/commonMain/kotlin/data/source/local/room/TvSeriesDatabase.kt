package data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import data.source.local.entity.TvFavoriteEntity
import data.source.local.entity.TvSeriesEntity

@Database(
    entities = [TvSeriesEntity::class, TvFavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TvSeriesDatabase : RoomDatabase(), DB {
    abstract fun tvSeriesDao(): TvSeriesDao

    abstract fun tvFavoriteDao(): TvSeriesFavoriteDao


    override fun clearAllTables(): Unit {}
}

interface DB {
    fun clearAllTables(): Unit {}
}