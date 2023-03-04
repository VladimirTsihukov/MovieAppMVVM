package com.androidapp.movieappmvvm.api.dataApi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoviesList (
    val results: List<Movie>,
): Parcelable
