package com.androidapp.movieappmvvm.data.dataApi

import android.os.Parcelable
import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies
import com.androidapp.movieappmvvm.model.api.ApiUtils.BASE_URL_MOVIE_IMAGE
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(

    val id: Long,

    val title: String,

    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Long,

    val adult: Boolean,

    var typeMovie: String,
    var likeMovie: Boolean = false,

    ) : Parcelable

fun Movie.parsInDataDBMovies(): DataDBMovies {
    return this.run {
        @Suppress("unused")
        (DataDBMovies(
        id = id,
        title = title,
        overview = overview,
        posterPath = BASE_URL_MOVIE_IMAGE + posterPath,
        backdropPath = BASE_URL_MOVIE_IMAGE + backdropPath,
        ratings = voteAverage,
        numberOfRatings = voteCount,
        minimumAge = if (adult) 16 else 13,
        likeMovie = likeMovie,
        typeMovie = typeMovie
    ))
    }
}

fun parsInDataDBMoviesList(moviePopular: String, listMovie: List<Movie>): List<DataDBMovies> {
    val listDataDBMovies = mutableListOf<DataDBMovies>()
    listMovie.forEach {
        listDataDBMovies.add(
            DataDBMovies(
                id = it.id,
                title = it.title,
                overview = it.overview,
                posterPath = BASE_URL_MOVIE_IMAGE + it.posterPath,
                backdropPath = BASE_URL_MOVIE_IMAGE + it.backdropPath,
                ratings = it.voteAverage,
                numberOfRatings = it.voteCount,
                minimumAge = if (it.adult) 16 else 13,
                likeMovie = it.likeMovie,
                typeMovie = moviePopular
            )
        )
    }
    return listDataDBMovies
}