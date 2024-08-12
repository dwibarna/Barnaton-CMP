package data.source.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvSeriesResponse (
    @SerialName("page")
    val page: Int? = null,

    @SerialName("total_pages")
    val totalPages: Int? = null,

    @SerialName("results")
    val results: List<TvSeriesItem>? = null,

    @SerialName("total_results")
    val totalResults: Int? = null
)