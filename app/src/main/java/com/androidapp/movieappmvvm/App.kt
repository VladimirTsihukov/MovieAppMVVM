package com.androidapp.movieappmvvm

import android.app.Application
import com.androidapp.movieappmvvm.di.components.AppComponent
import com.androidapp.movieappmvvm.di.components.DaggerAppComponent

class App : Application() {

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
}