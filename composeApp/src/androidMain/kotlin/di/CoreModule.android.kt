package di

import com.barna.barnaton.database.getTvSeriesDatabase
import data.source.local.room.TvSeriesDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<TvSeriesDatabase> { getTvSeriesDatabase(get()) }
}