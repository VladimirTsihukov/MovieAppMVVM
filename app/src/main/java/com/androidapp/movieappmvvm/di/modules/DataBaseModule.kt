package com.androidapp.movieappmvvm.di.modules

import android.content.Context
import androidx.room.Room
import com.androidapp.movieappmvvm.di.components.AppScope
import com.androidapp.movieappmvvm.model.database.DatabaseContact
import com.androidapp.movieappmvvm.model.database.databaseMoviesList.DbMovies
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