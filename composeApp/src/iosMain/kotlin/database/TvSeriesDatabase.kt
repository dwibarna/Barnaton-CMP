package database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.source.local.room.TvSeriesDatabase
import data.source.local.room.instantiateImpl
import platform.Foundation.NSHomeDirectory

fun getTvSeriesDatabase(): TvSeriesDatabase {
    val dbFile = NSHomeDirectory() + "/tv.db"
    return Room.databaseBuilder<TvSeriesDatabase>(
        name = dbFile,
        factory = { TvSeriesDatabase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}