package di

import data.repository.TvSeriesRepository
import data.repository.TvSeriesRepositoryImpl
import data.source.local.LocalDataSource
import data.source.remote.RemoteDataSource
import domain.usecase.TvSeriesUseCase
import domain.usecase.TvSeriesUseCaseImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import ui.screen.home.HomeViewModel
import ui.screen.detail.DetailViewModel
import ui.screen.favorite.FavoriteViewModel

expect fun platformModule(): Module


val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
        }
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<TvSeriesRepository> { TvSeriesRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
    factory<TvSeriesUseCase> { TvSeriesUseCaseImpl(get()) }
}

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::FavoriteViewModel)
    viewModelOf(::DetailViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) =
    startKoin {
        config?.invoke(this)
        modules(
            platformModule(),
            networkModule,
            repositoryModule,
            useCaseModule,
            viewModelModule
        )
    }
