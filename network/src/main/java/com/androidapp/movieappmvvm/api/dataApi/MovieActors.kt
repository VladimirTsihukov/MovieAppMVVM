package com.androidapp.movieappmvvm.api.dataApi

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieActors (
    val id: Long,
    val cast: List<Cast>,
) : Parcelable

@Parcelize
data class Cast (

    val id: Long,

    val name: String,

    @SerializedName("profile_path")
    val profilePath: String? = null,

) : Parcelable

data class ActorsInfo(
    val nameActor: String,
    val profilePath: String? = null,
)

fun getListActor(listCast: List<Cast>): List<ActorsInfo> {
    val resultList = mutableListOf<ActorsInfo>()
    listCast.forEach {
        resultList.add(ActorsInfo(nameActor = it.name, profilePath = it.profilePath))
    }
    return resultList
}