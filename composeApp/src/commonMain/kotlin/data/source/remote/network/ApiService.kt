package data.source.remote.network

import data.source.remote.RemoteDataSource
import data.source.remote.RemoteDataSource.Companion
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import utils.NetworkError
import utils.Result

class ApiService(private val httpClient: HttpClient) {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_API_KEY = "2174d146bb9c0eab47529b2e77d6b526"
    }

/*    suspend fun getTopRated(): HttpResponse {
        return try {
            httpClient.get(
                urlString = RemoteDataSource.BASE_URL + "tv/top_rated"
            ) {
                parameter(
                    "api_key",
                    RemoteDataSource.BASE_API_KEY
                )
            }
        } catch (e: UnresolvedAddressException) {
            Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            Result.Error(NetworkError.SERIALIZATION)
        }
    }*/


    suspend fun getPopular(): HttpResponse {
        return httpClient.get(
            urlString = BASE_URL +"tv/popular"
        ) {
            parameter(
                "api_key",
                BASE_API_KEY
            )
        }
    }

    suspend fun getOnTheAir(): HttpResponse {
        return httpClient.get(
            urlString = BASE_URL +"tv/on_the_air"
        ) {
            parameter(
                "api_key",
                BASE_API_KEY
            )
        }
    }
/*    @GET("tv/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY
    ): TvSeriesResponse

    @GET("tv/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY
    ): TvSeriesResponse

    @GET("tv/on_the_air")
    suspend fun getOnTheAir(
        @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY
    ): TvSeriesResponse

    @GET("tv/{id}")
    suspend fun getDetailTvSeries(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
    ): TvSeriesDetailResponse

    @GET("search/tv")
    suspend fun getSearchTvSeries(
        @Query("api_key") apiKey: String = BuildConfig.BASE_API_KEY,
        @Query("query") query: String
    ): TvSeriesResponse*/
}



/*

package com.example.barnaton.di

import com.example.barnaton.BuildConfig
import com.example.barnaton.data.remote.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor(
        HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        )
    )
        .connectTimeout(
            timeout = 120,
            unit = TimeUnit.SECONDS
        )
        .readTimeout(
            timeout = 120,
            unit = TimeUnit.SECONDS
        )
        .build()


    @Provides
    fun provideApiService(client: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
 */