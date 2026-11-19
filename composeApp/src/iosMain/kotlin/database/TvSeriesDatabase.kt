package database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.source.local.room.TvSeriesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSHomeDirectory

fun getTvSeriesDatabase(): TvSeriesDatabase {
    val dbFile = NSHomeDirectory() + "/Documents/tv.db"
    return Room.databaseBuilder<TvSeriesDatabase>(
        name = dbFile
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
