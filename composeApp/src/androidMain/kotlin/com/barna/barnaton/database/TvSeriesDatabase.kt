package com.barna.barnaton.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import data.source.local.room.TvSeriesDatabase

fun getTvSeriesDatabase(context: Context): TvSeriesDatabase {
    val dbFile = context.getDatabasePath("tv.db")
    return Room.databaseBuilder<TvSeriesDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}