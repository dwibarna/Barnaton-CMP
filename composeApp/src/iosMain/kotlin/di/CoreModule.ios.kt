package di

import database.getTvSeriesDatabase
import org.koin.dsl.module

actual fun platformModule() = module {
    single { getTvSeriesDatabase() }
}