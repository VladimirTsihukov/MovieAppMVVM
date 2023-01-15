package com.androidapp.movieappmvvm.data.dataApi

import android.os.Parcelable
import com.androidapp.movieappmvvm.data.dataDb.DataDBMoviesDetails
import com.androidapp.movieappmvvm.model.api.ApiUtils.BASE_URL_MOVIE_IMAGE
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoviesDetail(

    val id: Long,

    val title: String,

    val overview: String,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    @SerializedName("vote_average")
    val ratings: Double,

    @SerializedName("vote_count")
    val voteCount: Long,

    val adult: Boolean,

    val runtime: Long,

    val genres: List<Genre>,
): Parcelable

fun MoviesDetail.getMovieDetails(): DataDBMoviesDetails {

    return this.run {
        @Suppress("unused")
        (DataDBMoviesDetails(
        id = id,
        title = title,
        overview = overview,
        backdrop = BASE_URL_MOVIE_IMAGE + backdropPath,
        ratings = ratings,
        numberOfRatings = voteCount.toInt(),
        minimumAge = if (adult) 16 else 13,
        runtime = runtime,
        genres = genres.joinToString(", ") { it.name },
    ))
    }
}

@Parcelize
data class Genre(
    val id: Long,
    val name: String,
) : Parcelable
