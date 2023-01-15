package com.androidapp.movieappmvvm.di.components

import androidx.fragment.app.Fragment
import com.androidapp.movieappmvvm.App
import com.androidapp.movieappmvvm.utils.ViewModelProvider
import com.androidapp.movieappmvvm.view.ui.viewModel.ViewModelMovieList
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MovieListScope

@MovieListScope
@Component(dependencies = [AppComponent::class])
interface MovieListComponent : ViewModelProvider<ViewModelMovieList>

val Fragment.movieListComponent: MovieListComponent?
    get() = DaggerMovieListComponent.builder()
        .appComponent((activity?.application as App).appComponent)
        .build()

