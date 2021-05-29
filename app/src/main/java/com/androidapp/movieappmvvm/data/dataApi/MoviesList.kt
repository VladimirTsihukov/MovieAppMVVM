package com.androidapp.movieappmvvm.data.dataApi

import android.os.Parcelable
import com.androidapp.movieappmvvm.data.dataApi.Movie
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoviesList (
    val results: List<Movie>,
): Parcelable
