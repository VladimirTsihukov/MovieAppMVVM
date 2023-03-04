package com.androidapp.movieappmvvm.di.components

import androidx.fragment.app.Fragment
import com.androidapp.movieappmvvm.App
import com.androidapp.movieappmvvm.data.database.databaseMoviesList.DbMovies
import com.androidapp.movieappmvvm.view.ui.viewModel.ViewModelMovieList
import com.androidapp.moviesappmvvm.feature.movies_detail.CustomViewModelProvider
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MovieListScope

@MovieListScope
@Component(dependencies = [AppComponent::class])
interface MovieListComponent : CustomViewModelProvider<ViewModelMovieList> {
    fun getDbMovies(): DbMovies
}

val Fragment.movieListComponent: MovieListComponent?
    get() = DaggerMovieListComponent.builder()
        .appComponent((activity?.application as App).appComponent)
        .build()

