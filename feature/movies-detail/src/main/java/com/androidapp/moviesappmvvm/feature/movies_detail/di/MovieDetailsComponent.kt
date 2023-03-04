package com.androidapp.moviesappmvvm.feature.movies_detail.di

import androidx.fragment.app.Fragment
import com.androidapp.movieappmvvm.api.ApiService
import com.androidapp.movieappmvvm.api.NetworkStatusLiveData
import com.androidapp.movieappmvvm.data.database.databaseMoviesList.DbMovies
import com.androidapp.moviesappmvvm.feature.movies_detail.CustomViewModelProvider
import com.androidapp.moviesappmvvm.feature.movies_detail.ui.viewModel.ViewModelMovieDetails
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MovieDetailsScope

@MovieDetailsScope
@Component(
    dependencies = [MovieDetailsComponentDependencies::class]
)
interface MovieDetailsComponent : CustomViewModelProvider<ViewModelMovieDetails> {
    fun getApiService(): ApiService
}

val Fragment.movieDetailsComponent: MovieDetailsComponent
    get() = DaggerMovieDetailsComponent.builder()
        .movieDetailsComponentDependencies((requireContext().applicationContext as MovieDetailsComponentProvider).getMovieDetailsComponentDependencies())
        .build()

interface MovieDetailsComponentDependencies {
    val apiService: ApiService
    val dbMovies: DbMovies
    val networkStatusLiveData: NetworkStatusLiveData
}

interface MovieDetailsComponentProvider {
    fun getMovieDetailsComponentDependencies(): MovieDetailsComponentDependencies
}