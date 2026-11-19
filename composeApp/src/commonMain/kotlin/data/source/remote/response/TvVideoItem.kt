package data.source.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvVideoItem(
    @SerialName("id")
    val id: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("key")
    val key: String? = null,

    @SerialName("site")
    val site: String? = null,

    @SerialName("size")
    val size: Int? = null,

    @SerialName("type")
    val type: String? = null,

    @SerialName("official")
    val official: Boolean? = null,

    @SerialName("published_at")
    val publishedAt: String? = null
)
