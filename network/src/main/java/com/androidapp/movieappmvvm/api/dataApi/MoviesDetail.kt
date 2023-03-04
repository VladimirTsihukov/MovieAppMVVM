package com.androidapp.movieappmvvm.api.dataApi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class Genre(
    val id: Long,
    val name: String,
) : Parcelable
