package domain.model

data class TvSeries(
    val id: Int = 0,

    val firstAirDate: String,

    val overview: String,

    val originalLanguage: String,

    val genreIds: List<Int>,

    val posterPath: String,

    val originCountry: List<String>,

    val backdropPath: String,

    val originalName: String,

    val voteAverage: Float,

    val name: String,

    val adult: Boolean,

    val voteCount: Float,
)