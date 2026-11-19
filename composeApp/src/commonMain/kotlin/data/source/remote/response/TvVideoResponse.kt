package data.source.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvVideoResponse(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("results")
    val results: List<TvVideoItem>? = null
)
