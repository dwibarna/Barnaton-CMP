package data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tv_favorite")
data class TvFavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("backdrop_path")
    val backdropPath: String? = null,
    @ColumnInfo("name")
    val name: String? = null,
    @ColumnInfo("overview")
    val overview: String? = null,
)