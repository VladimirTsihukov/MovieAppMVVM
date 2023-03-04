package com.androidapp.movieappmvvm.di.components

import android.app.Activity
import android.content.Context
import com.androidapp.movieappmvvm.App
import com.androidapp.movieappmvvm.api.ApiService
import com.androidapp.movieappmvvm.api.NetworkStatusLiveData
import com.androidapp.movieappmvvm.data.database.databaseMoviesList.DbMovies
import com.androidapp.movieappmvvm.data.database.module.DataBaseModule
import com.androidapp.movieappmvvm.di.AppScope
import com.androidapp.movieappmvvm.di.modules.ApiModule
import com.androidapp.movieappmvvm.view.ui.activity.ActivityMain
import com.androidapp.moviesappmvvm.feature.movies_detail.di.MovieDetailsComponentDependencies
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [ApiModule::class, DataBaseModule::class])
interface AppComponent : MovieDetailsComponentDependencies {

    override val apiService: ApiService
    override val dbMovies: DbMovies
    override val networkStatusLiveData: NetworkStatusLiveData

    fun inject(activityMain: ActivityMain)

    @Component.Factory
    interface AppFactor {
        fun create(@BindsInstance context: Context): AppComponent
    }
}

val Context.appComponent: AppComponent
    get() = (applicationContext as App).appComponent

val Activity.appComponent: AppComponent
    get() = (application as App).appComponent