package com.androidapp.movieappmvvm.data.database.module

import android.content.Context
import androidx.room.Room
import com.androidapp.movieappmvvm.data.database.DatabaseContact
import com.androidapp.movieappmvvm.data.database.databaseMoviesList.DbMovies
import com.androidapp.movieappmvvm.di.AppScope
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {

    @AppScope
    @Provides
    fun getDbMovies(context: Context): DbMovies {
        return Room.databaseBuilder(
            context,
            DbMovies::class.java,
            DatabaseContact.DATABASE_NAME_MOVIES
        ).fallbackToDestructiveMigration().build()
    }
}