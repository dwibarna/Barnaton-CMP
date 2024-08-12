package data.source.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvSeriesDetailResponse(

    @SerialName("original_language")
    val originalLanguage: String? = null,

    @SerialName("number_of_episodes")
    val numberOfEpisodes: Int? = null,

    @SerialName("type")
    val type: String? = null,

    @SerialName("backdrop_path")
    val backdropPath: String? = null,

    @SerialName("popularity")
    val popularity: Float? = null,

    @SerialName("id")
    val id: Int? = null,

    @SerialName("number_of_seasons")
    val numberOfSeasons: Int? = null,

    @SerialName("vote_count")
    val voteCount: Int? = null,

    @SerialName("first_air_date")
    val firstAirDate: String? = null,

    @SerialName("overview")
    val overview: String? = null,

    @SerialName("languages")
    val languages: List<String?>? = null,

    @SerialName("poster_path")
    val posterPath: String? = null,

    @SerialName("origin_country")
    val originCountry: List<String?>? = null,

    @SerialName("original_name")
    val originalName: String? = null,

    @SerialName("vote_average")
    val voteAverage: Float? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("tagline")
    val tagline: String? = null,

    @SerialName("episode_run_time")
    val episodeRunTime: List<Int>? = null,

    @SerialName("adult")
    val adult: Boolean? = null,

    @SerialName("in_production")
    val inProduction: Boolean? = null,

    @SerialName("last_air_date")
    val lastAirDate: String? = null,

    @SerialName("homepage")
    val homepage: String? = null,

    @SerialName("status")
    val status: String? = null
)

