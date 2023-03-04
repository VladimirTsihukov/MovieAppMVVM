package com.androidapp.movieappmvvm.data.dataDb

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androidapp.movieappmvvm.data.database.DatabaseContact
import kotlinx.android.parcel.Parcelize

@Entity(tableName = DatabaseContact.TABLE_NAME_MOVIES_LIKE)
@Parcelize
data class DataDBMoviesLike(
    @PrimaryKey
    val idLike: Long,
    val titleLike: String,
    val overviewLike: String,
    val posterPathLike: String,
    val backdropPathLike: String,
    val ratingsLike: Double,
    val numberOfRatingsLike: Long,
    val minimumAgeLike: Int,
    var likeMovieLike: Boolean = false,
    val typeMovieLike: String,
) : Parcelable
