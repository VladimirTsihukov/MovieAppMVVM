package com.androidapp.movieappmvvm

import android.app.Application
import com.androidapp.movieappmvvm.model.database.databaseMoviesList.DbMovies
import com.androidapp.movieappmvvm.view.network.NetworkStatusLiveData

class App : Application() {

    companion object {
        lateinit var instance : App
        lateinit var dbMovies: DbMovies
        lateinit var networkStatusLiveData: NetworkStatusLiveData
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        dbMovies = DbMovies.instance(this)
        networkStatusLiveData = NetworkStatusLiveData(this)
    }
}