package com.androidapp.movieappmvvm.data.database.databaseMoviesList

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies
import com.androidapp.movieappmvvm.data.dataDb.DataDBMoviesDetails
import com.androidapp.movieappmvvm.data.dataDb.DataDBMoviesLike
import com.androidapp.movieappmvvm.data.database.dao.DaoMovieDetails
import com.androidapp.movieappmvvm.data.database.dao.DaoMovies
import com.androidapp.movieappmvvm.data.database.dao.DaoMoviesLike

@Database(
    entities = [DataDBMovies::class, DataDBMoviesLike::class, DataDBMoviesDetails::class],
    version = 1,
)
abstract class DbMovies : RoomDatabase() {

    abstract fun movies(): DaoMovies
    abstract fun moviesLike(): DaoMoviesLike
    abstract fun moviesDetails(): DaoMovieDetails
}