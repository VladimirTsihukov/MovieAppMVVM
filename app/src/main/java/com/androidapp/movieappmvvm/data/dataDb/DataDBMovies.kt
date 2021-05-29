package com.androidapp.movieappmvvm.data.dataDb

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androidapp.movieappmvvm.model.database.DatabaseContact.TABLE_NAME_MOVIES
import kotlinx.android.parcel.Parcelize

@Entity (tableName = TABLE_NAME_MOVIES)
@Parcelize
data class DataDBMovies (
    @PrimaryKey
    val id: Long,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val ratings: Double,
    val numberOfRatings: Long,
    val minimumAge: Int,

    val typeMovie: String,
    var likeMovie: Boolean
    ) : Parcelable


fun DataDBMovies.parsInDataDBMoviesLike(): DataDBMoviesLike {
    return this.run {
        @Suppress("unused")
        (DataDBMoviesLike(
        idLike = id,
        titleLike = title,
        overviewLike = overview,
        posterPathLike = posterPath,
        backdropPathLike = backdropPath,
        ratingsLike = ratings,
        numberOfRatingsLike = numberOfRatings,
        minimumAgeLike = minimumAge,
        likeMovieLike = likeMovie,
        typeMovieLike = typeMovie
    ))
    }
}

fun parsInDataDataDBMovies(listMovie: List<DataDBMoviesLike>): List<DataDBMovies> {
    val listDataDBMovies = mutableListOf<DataDBMovies>()
    listMovie.forEach {
        listDataDBMovies.add(
            DataDBMovies(
                id = it.idLike,
                title = it.titleLike,
                overview = it.overviewLike,
                posterPath = it.posterPathLike,
                backdropPath = it.backdropPathLike,
                ratings = it.ratingsLike,
                numberOfRatings = it.numberOfRatingsLike,
                minimumAge = it.minimumAgeLike,
                likeMovie = it.likeMovieLike,
                typeMovie = it.typeMovieLike
            )
        )
    }
    return listDataDBMovies
}