package com.androidapp.movieappmvvm.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies

@Dao
interface DaoMovies {

    @Query("SELECT * FROM tableMovies WHERE typeMovie = :moviePopular ORDER BY ratings DESC")
    suspend fun getMoviesCategory(moviePopular: String): List<DataDBMovies>

    @Query("DELETE FROM tableMovies WHERE typeMovie = :moviePopular")
    suspend fun deleteMoviesCategory(moviePopular: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<DataDBMovies>)

    @Query("UPDATE tableMovies SET likeMovie = :likeMovie WHERE id = :id")
    suspend fun setMoviesIdLikeInDb(id : Long, likeMovie : Boolean)
}