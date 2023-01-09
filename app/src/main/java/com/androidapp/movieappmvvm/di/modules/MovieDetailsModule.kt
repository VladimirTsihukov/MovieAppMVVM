package com.androidapp.movieappmvvm.di.modules

import androidx.lifecycle.ViewModel
import com.androidapp.movieappmvvm.di.components.MovieDetailsScope
import com.androidapp.movieappmvvm.model.api.ApiService
import com.androidapp.movieappmvvm.model.database.databaseMoviesList.DbMovies
import com.androidapp.movieappmvvm.view.network.NetworkStatusLiveData
import com.androidapp.movieappmvvm.view.ui.viewModel.ViewModelMovieDetails
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap


@Module
class MovieDetailsModule {

    @MovieDetailsScope
    @IntoMap
    @ViewModelKey(ViewModelMovieDetails::class)
    @Provides
    fun getViewModel(
        apiService: ApiService,
        dbMovies: DbMovies,
        networkStatusLiveData: NetworkStatusLiveData,
    ): ViewModel {
        return ViewModelMovieDetails(apiService, dbMovies, networkStatusLiveData)
    }
}
