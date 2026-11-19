package data.source.local.room

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import data.source.local.entity.TvFavoriteEntity
import data.source.local.entity.TvSeriesEntity

@Database(
    entities = [TvSeriesEntity::class, TvFavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(TvSeriesDatabaseConstructor::class)
abstract class TvSeriesDatabase : RoomDatabase() {
    abstract fun tvSeriesDao(): TvSeriesDao

    abstract fun tvFavoriteDao(): TvSeriesFavoriteDao
}

@Suppress("KotlinNoActualForExpect")
expect object TvSeriesDatabaseConstructor : RoomDatabaseConstructor<TvSeriesDatabase> {
    override fun initialize(): TvSeriesDatabase
}
