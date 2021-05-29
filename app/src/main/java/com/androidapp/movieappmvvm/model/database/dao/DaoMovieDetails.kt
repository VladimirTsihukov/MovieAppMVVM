package com.androidapp.movieappmvvm.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidapp.movieappmvvm.data.dataDb.DataDBMoviesDetails

@Dao
interface DaoMovieDetails {

    @Query("SELECT * FROM tableMoviesDetails ORDER BY id")
    suspend fun getAllMoviesDetails(): List<DataDBMoviesDetails>?

    @Query("SELECT * FROM tableMoviesDetails WHERE id = :idMovie LIMIT 1")
    suspend fun getMovieDetail(idMovie: Long): DataDBMoviesDetails?

    @Query("UPDATE tableMoviesDetails SET nameActors = :name WHERE id = :id")
    suspend fun setNameActors(name: String, id: Long)

    @Query("UPDATE tableMoviesDetails SET profilePaths = :profilePaths WHERE id = :id")
    suspend fun setProfilePaths(profilePaths: String, id: Long)

    @Query("SELECT nameActors FROM tableMoviesDetails WHERE id=:id")
    suspend fun getNameActors(id: Long): String?

    @Query("SELECT profilePaths FROM tableMoviesDetails WHERE id=:id")
    suspend fun getProfilePaths(id: Long): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetail: DataDBMoviesDetails)
}