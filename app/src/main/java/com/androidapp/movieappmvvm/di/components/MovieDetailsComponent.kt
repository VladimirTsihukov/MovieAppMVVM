package com.androidapp.movieappmvvm.di.components

import androidx.fragment.app.Fragment
import com.androidapp.movieappmvvm.App
import com.androidapp.movieappmvvm.di.modules.MovieDetailsModule
import com.androidapp.movieappmvvm.utils.ViewModelFactory
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MovieDetailsScope

@MovieDetailsScope
@Component(dependencies = [AppComponent::class], modules = [MovieDetailsModule::class])
interface MovieDetailsComponent {

    fun getViewModelFactory(): ViewModelFactory
}

val Fragment.movieDetailsComponent: MovieDetailsComponent?
    get() = DaggerMovieDetailsComponent.builder()
        .appComponent((activity?.application as App).appComponent)
        .build()