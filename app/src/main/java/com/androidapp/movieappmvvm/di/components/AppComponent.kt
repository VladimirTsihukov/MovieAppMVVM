package com.androidapp.movieappmvvm.di.components

import android.app.Activity
import android.content.Context
import com.androidapp.movieappmvvm.App
import com.androidapp.movieappmvvm.di.modules.ApiModule
import com.androidapp.movieappmvvm.di.modules.DataBaseModule
import com.androidapp.movieappmvvm.model.api.ApiService
import com.androidapp.movieappmvvm.model.database.databaseMoviesList.DbMovies
import com.androidapp.movieappmvvm.view.network.NetworkStatusLiveData
import com.androidapp.movieappmvvm.view.ui.activity.ActivityMain
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@AppScope
@Component(modules = [ApiModule::class, DataBaseModule::class])
interface AppComponent {

    fun getDbMovies(): DbMovies
    fun getApiService(): ApiService
    fun getNetworkStatusLiveData(): NetworkStatusLiveData

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