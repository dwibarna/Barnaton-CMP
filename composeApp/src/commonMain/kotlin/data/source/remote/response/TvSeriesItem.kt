package data.source.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvSeriesItem(

    @SerialName("first_air_date")
    val firstAirDate: String? = null,

    @SerialName("overview")
    val overview: String? = null,

    @SerialName("original_language")
    val originalLanguage: String? = null,

    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,

    @SerialName("poster_path")
    val posterPath: String? = null,

    @SerialName("origin_country")
    val originCountry: List<String>? = null,

    @SerialName("backdrop_path")
    val backdropPath: String? = null,

    @SerialName("original_name")
    val originalName: String? = null,

    @SerialName("popularity")
    val popularity: Float? = null,

    @SerialName("vote_average")
    val voteAverage: Float? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("id")
    val id: Int? = null,

    @SerialName("adult")
    val adult: Boolean? = null,

    @SerialName("vote_count")
    val voteCount: Float? = null,
)