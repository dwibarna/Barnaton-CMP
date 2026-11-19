package data.source.remote

import data.source.remote.response.TvSeriesDetailResponse
import data.source.remote.response.TvSeriesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import utils.AppConfig
import utils.NetworkError
import utils.Result as Results

class RemoteDataSource(private val httpClient: HttpClient) {

    private suspend inline fun <reified T> get(
        path: String,
        queryParams: Map<String, String> = emptyMap()
    ): Results<T, NetworkError> {
        val response = try {
            httpClient.get(urlString = AppConfig.BASE_URL + path) {
                parameter("api_key", AppConfig.TMDB_API_KEY)
                queryParams.forEach { (key, value) -> parameter(key, value) }
            }
        } catch (e: UnresolvedAddressException) {
            return Results.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Results.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> Results.Success(response.body<T>())
            401 -> Results.Error(NetworkError.UNAUTHORIZED)
            408 -> Results.Error(NetworkError.REQUEST_TIMEOUT)
            409 -> Results.Error(NetworkError.CONFLICT)
            413 -> Results.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> Results.Error(NetworkError.SERVER_ERROR)
            else -> Results.Error(NetworkError.UNKNOWN)
        }
    }

    suspend fun getTopRated(): Results<TvSeriesResponse, NetworkError> =
        get("tv/top_rated")

    suspend fun getPopular(): Results<TvSeriesResponse, NetworkError> =
        get("tv/popular")

    suspend fun getOnTheAir(): Results<TvSeriesResponse, NetworkError> =
        get("tv/on_the_air")

    suspend fun getDetailTvSeries(id: Int): Results<TvSeriesDetailResponse, NetworkError> =
        get("tv/$id")

    fun getSearchTvSeries(query: String): Flow<Results<TvSeriesResponse, NetworkError>> = flow {
        emit(get("search/tv", mapOf("query" to query)))
    }
}
