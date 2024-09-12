package data.source.remote

import data.source.remote.response.TvSeriesDetailResponse
import data.source.remote.response.TvSeriesResponse
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import utils.NetworkError
import utils.Result as Results


class RemoteDataSource(private val httpClient: HttpClient) {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_API_KEY = "2174d146bb9c0eab47529b2e77d6b526"
    }

    suspend fun getTopRated(): Results<TvSeriesResponse, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = BASE_URL + "tv/top_rated"
            ) {
                parameter(
                    "api_key",
                    BASE_API_KEY
                )
            }
        } catch (e: UnresolvedAddressException) {
            return Results.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Results.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> Results.Success(response.body<TvSeriesResponse>())
            401 -> Results.Error(NetworkError.UNAUTHORIZED)
            409 -> Results.Error(NetworkError.CONFLICT)
            408 -> Results.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Results.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Results.Error(NetworkError.SERVER_ERROR)
            else -> Results.Error(NetworkError.UNKNOWN)
        }
    }

/*    suspend fun getSearchTvSeries(query: String): Results<TvSeriesResponse, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = BASE_URL + "search/tv"
            ) {
                parameter(
                    "api_key",
                    BASE_API_KEY
                )
                parameter(
                    "query",
                    query
                )
            }
        } catch (e: UnresolvedAddressException) {
            return Results.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Results.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> Results.Success(response.body<TvSeriesResponse>())
            401 -> Results.Error(NetworkError.UNAUTHORIZED)
            409 -> Results.Error(NetworkError.CONFLICT)
            408 -> Results.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Results.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Results.Error(NetworkError.SERVER_ERROR)
            else -> {
                Results.Error(NetworkError.UNKNOWN)
            }
        }
    }*/

    suspend fun getSearchTvSeries(query: String): Flow<Results<TvSeriesResponse, NetworkError>> = flow {
        val response = try {
            httpClient.get(
                urlString = BASE_URL + "search/tv"
            ) {
                parameter(
                    "query",
                    query
                )
                parameter(
                    "api_key",
                    BASE_API_KEY
                )
            }
        } catch (e: UnresolvedAddressException) {
            emit(Results.Error(NetworkError.NO_INTERNET))
            return@flow
        } catch (e: SerializationException) {
            emit(Results.Error(NetworkError.SERIALIZATION))
            return@flow
        }

         when (response.status.value) {
            in 200..299 -> emit(Results.Success(response.body<TvSeriesResponse>()))
            401 -> emit(Results.Error(NetworkError.UNAUTHORIZED))
            409 -> emit(Results.Error(NetworkError.CONFLICT))
            408 -> emit(Results.Error(NetworkError.REQUEST_TIMEOUT))
            413 -> emit(Results.Error(NetworkError.PAYLOAD_TOO_LARGE))
            in 500..599 -> emit(Results.Error(NetworkError.SERVER_ERROR))
            else -> emit(Results.Error(NetworkError.UNKNOWN))
        }
    }


    suspend fun getPopular(): Results<TvSeriesResponse, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = BASE_URL + "tv/popular"
            ) {
                parameter(
                    "api_key",
                    BASE_API_KEY
                )
            }
        } catch (e: UnresolvedAddressException) {
            return Results.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Results.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> Results.Success(response.body<TvSeriesResponse>())
            401 -> Results.Error(NetworkError.UNAUTHORIZED)
            409 -> Results.Error(NetworkError.CONFLICT)
            408 -> Results.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Results.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Results.Error(NetworkError.SERVER_ERROR)
            else -> {
                Results.Error(NetworkError.UNKNOWN)
            }
        }
    }


    suspend fun getOnTheAir(): Results<TvSeriesResponse, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = BASE_URL + "tv/on_the_air"
            ) {
                parameter(
                    "api_key",
                    BASE_API_KEY
                )
            }
        } catch (e: UnresolvedAddressException) {
            return Results.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Results.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> Results.Success(response.body<TvSeriesResponse>())
            401 -> Results.Error(NetworkError.UNAUTHORIZED)
            409 -> Results.Error(NetworkError.CONFLICT)
            408 -> Results.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Results.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Results.Error(NetworkError.SERVER_ERROR)
            else -> {
                Results.Error(NetworkError.UNKNOWN)
            }
        }
    }

    suspend fun getDetailTvSeries(id: Int): Results<TvSeriesDetailResponse, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = BASE_URL + "tv/$id"
            ) {
                parameter(
                    "api_key",
                    BASE_API_KEY
                )
            }
        } catch (e: UnresolvedAddressException) {
            return Results.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Results.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> Results.Success(response.body<TvSeriesDetailResponse>())
            401 -> Results.Error(NetworkError.UNAUTHORIZED)
            409 -> Results.Error(NetworkError.CONFLICT)
            408 -> Results.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> Results.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Results.Error(NetworkError.SERVER_ERROR)
            else -> Results.Error(NetworkError.UNKNOWN)
        }
    }



}