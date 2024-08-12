package utils

import data.source.local.entity.TvFavoriteEntity
import data.source.local.entity.TvSeriesEntity
import data.source.remote.response.TvSeriesDetailResponse
import data.source.remote.response.TvSeriesResponse
import domain.model.TvDetailSeries
import domain.model.TvFavorite
import domain.model.TvSeries

object DataMapper {
    fun tvResponseToEntity(data: TvSeriesResponse, type: String): List<TvSeriesEntity> {
        val array: MutableList<TvSeriesEntity> = mutableListOf()
        data.results?.forEach {
            array.add(
                TvSeriesEntity(
                    id = it.id ?: 0,
                    firstAirDate = it.firstAirDate,
                    overview = it.overview,
                    originalLanguage = it.originalLanguage,
                    posterPath = it.posterPath,
                    backdropPath = it.backdropPath,
                    originalName = it.originalName,
                    voteAverage = it.voteAverage,
                    name = it.name,
                    adult = it.adult,
                    voteCount = it.voteCount,
                    typeEntity = type
                )
            )
        }
        return array
    }

    fun tvEntityToDomain(data: List<TvSeriesEntity>): List<TvSeries> {
        val array: MutableList<TvSeries> = mutableListOf()
        data.forEach { dataa ->
            TvSeries(
                id = dataa.id,
                firstAirDate = dataa.firstAirDate ?: "",
                overview = dataa.overview ?: "No Overview",
                originalLanguage = dataa.originalLanguage ?: "id",
                posterPath = "https://image.tmdb.org/t/p/w500" + (dataa.posterPath ?: ""),
                backdropPath = "https://image.tmdb.org/t/p/w500" + (dataa.backdropPath ?: ""),
                originalName = dataa.originalName ?: "Untitled",
                voteAverage = dataa.voteAverage ?: 0F,
                name = dataa.name ?: "Untitled",
                adult = dataa.adult ?: false,
                voteCount = dataa.voteCount ?: 0F,
                genreIds = emptyList(),
                originCountry = emptyList()
            ).let(array::add)
        }
        return array
    }

    fun tvDetailResponseToDomain(data: TvSeriesDetailResponse): TvDetailSeries {
        with(data) {
            return TvDetailSeries(
                originalLanguage ?: "",
                numberOfEpisodes ?: 0,
                type ?: "",
                "https://image.tmdb.org/t/p/w500" + (backdropPath ?: ""),
                popularity ?: 0F,
                id ?: 0,
                numberOfSeasons ?: 0,
                voteCount ?: 0,
                firstAirDate ?: "",
                overview ?: "",
                "https://image.tmdb.org/t/p/w500" + (posterPath ?: ""),
                originalName ?: "",
                voteAverage ?: 0f,
                name ?: "",
                tagline ?: "",
                convertToDuration(episodeRunTime ?: emptyList()),
                adult ?: false,
                inProduction ?: false,
                lastAirDate ?: "",
                homepage ?: "",
                status ?: ""
            )
        }
    }

    private fun convertToDuration(episodeRunTime: List<Int>): String {
        var tempTime = 0
        episodeRunTime.forEach {
            tempTime += it
        }
        val time = tempTime.div(if (episodeRunTime.isEmpty()) 1 else episodeRunTime.size)
        val hours = time / 60
        val minutes = time % 60

        return if (hours > 0) {
            "$hours Hours $minutes Minutes"
        } else {
            "$minutes Minutes"
        }
    }

    fun tvFavoriteListEntityToDomain(data: List<TvFavoriteEntity>): List<TvFavorite> {
        val mutableList: MutableList<TvFavorite> = mutableListOf()

        data.forEach {
            TvFavorite(
                id = it.id,
                backdropPath = it.backdropPath ?: "",
                name = it.name ?: "",
                overview = it.overview ?: ""
            ).let(mutableList::add)
        }

        return mutableList
    }

    fun tvFavoriteDomainToEntity(entity: TvFavorite): TvFavoriteEntity {
        return TvFavoriteEntity(
            id = entity.id,
            backdropPath = entity.backdropPath,
            name = entity.name,
            overview = entity.overview
        )
    }

    fun tvFavoriteEntityToDomain(tvFavorite: TvFavoriteEntity?): TvFavorite {
        return TvFavorite(
            id = tvFavorite?.id ?: 0,
            backdropPath = tvFavorite?.backdropPath ?: "-",
            name = tvFavorite?.name ?: "-",
            overview = tvFavorite?.overview ?: "-"
        )
    }
}