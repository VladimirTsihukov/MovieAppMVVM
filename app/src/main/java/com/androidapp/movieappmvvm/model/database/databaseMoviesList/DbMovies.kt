package com.androidapp.movieappmvvm.model.database.databaseMoviesList

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies
import com.androidapp.movieappmvvm.data.dataDb.DataDBMoviesDetails
import com.androidapp.movieappmvvm.data.dataDb.DataDBMoviesLike
import com.androidapp.movieappmvvm.model.database.DatabaseContact
import com.androidapp.movieappmvvm.model.database.dao.DaoMovieDetails
import com.androidapp.movieappmvvm.model.database.dao.DaoMovies
import com.androidapp.movieappmvvm.model.database.dao.DaoMoviesLike

@Database(
    entities = [DataDBMovies::class,
        DataDBMoviesLike::class,
        DataDBMoviesDetails::class], version = 1
)
abstract class DbMovies : RoomDatabase() {

    abstract fun movies(): DaoMovies
    abstract fun moviesLike(): DaoMoviesLike
    abstract fun moviesDetails(): DaoMovieDetails

    companion object {
        fun instance(context: Context): DbMovies {
            return Room.databaseBuilder(
                context,
                DbMovies::class.java,
                DatabaseContact.DATABASE_NAME_MOVIES
            ).fallbackToDestructiveMigration().build()
        }
    }
}