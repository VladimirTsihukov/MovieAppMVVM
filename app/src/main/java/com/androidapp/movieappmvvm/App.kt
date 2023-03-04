package com.androidapp.movieappmvvm

import android.app.Application
import com.androidapp.movieappmvvm.di.components.AppComponent
import com.androidapp.movieappmvvm.di.components.DaggerAppComponent
import com.androidapp.moviesappmvvm.feature.movies_detail.di.MovieDetailsComponentDependencies
import com.androidapp.moviesappmvvm.feature.movies_detail.di.MovieDetailsComponentProvider

class App : Application(), MovieDetailsComponentProvider {

    lateinit var appComponent: AppComponent
        private set

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }

    override fun getMovieDetailsComponentDependencies(): MovieDetailsComponentDependencies {
        return appComponent
    }
}