package com.androidapp.movieappmvvm.di.modules

import androidx.lifecycle.ViewModel
import com.androidapp.movieappmvvm.di.components.MovieListScope
import com.androidapp.movieappmvvm.model.api.ApiService
import com.androidapp.movieappmvvm.model.database.databaseMoviesList.DbMovies
import com.androidapp.movieappmvvm.view.network.NetworkStatusLiveData
import com.androidapp.movieappmvvm.view.ui.viewModel.ViewModelMovieList
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class MovieListModule {

    @MovieListScope
    @IntoMap
    @ViewModelKey(ViewModelMovieList::class)
    @Provides
    fun provideViewModelList(
        apiService: ApiService,
        dbMovies: DbMovies,
        networkStatusLiveData: NetworkStatusLiveData,
    ): ViewModel {
        return ViewModelMovieList(apiService, dbMovies, networkStatusLiveData)
    }
}