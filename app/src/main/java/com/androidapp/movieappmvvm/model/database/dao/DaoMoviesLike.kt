package com.androidapp.movieappmvvm.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androidapp.movieappmvvm.data.dataDb.DataDBMoviesLike

@Dao
interface DaoMoviesLike {

    @Query("SELECT * FROM tableMoviesLike ORDER BY ratingsLike")
    suspend fun getMoviesLike(): List<DataDBMoviesLike>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetMoviesLike(moviesLike: DataDBMoviesLike)

    @Query("DELETE FROM tableMoviesLike WHERE idLike = :id")
    suspend fun deleteMoviesLike(id: Long)

    @Query("SELECT idLike FROM tableMoviesLike")
    suspend fun getAllID() : List<Long>
}