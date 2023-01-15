package com.androidapp.movieappmvvm.di.components

import androidx.fragment.app.Fragment
import com.androidapp.movieappmvvm.App
import com.androidapp.movieappmvvm.di.modules.MovieListModule
import com.androidapp.movieappmvvm.utils.ViewModelFactory
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MovieListScope

@MovieListScope
@Component(modules = [MovieListModule::class], dependencies = [AppComponent::class])
interface MovieListComponent {

    fun getViewModelFactory(): ViewModelFactory
}

val Fragment.movieListComponent: MovieListComponent?
    get() = DaggerMovieListComponent.builder()
        .appComponent((activity?.application as App).appComponent)
        .build()

