package domain.model

data class TvDetailSeries(
    val originalLanguage: String,
    val numberOfEpisodes: Int,
    val type: String,
    val backdropPath: String,
    val popularity: Float,
    val id: Int,
    val numberOfSeasons: Int,
    val voteCount: Int,
    val firstAirDate: String,
    val overview: String,
    val posterPath: String,
    val originalName: String,
    val voteAverage: Float,
    val name: String,
    val tagline: String,
    val episodeRunTime: String,
    val adult: Boolean,
    val inProduction: Boolean,
    val lastAirDate: String,
    val homepage: String,
    val status: String
)