package com.androidapp.movieappmvvm.api.dataApi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
