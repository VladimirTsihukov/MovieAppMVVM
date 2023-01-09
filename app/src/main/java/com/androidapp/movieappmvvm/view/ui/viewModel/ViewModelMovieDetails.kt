package com.androidapp.movieappmvvm.view.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androidapp.movieappmvvm.data.dataApi.ActorsInfo
import com.androidapp.movieappmvvm.data.dataApi.MovieActors
import com.androidapp.movieappmvvm.data.dataApi.getListActor
import com.androidapp.movieappmvvm.data.dataApi.getMovieDetails
import com.androidapp.movieappmvvm.data.dataDb.DataDBMoviesDetails
import com.androidapp.movieappmvvm.model.api.ApiService
import com.androidapp.movieappmvvm.model.database.DatabaseContact.SEPARATOR
import com.androidapp.movieappmvvm.model.database.databaseMoviesList.DbMovies
import com.androidapp.movieappmvvm.view.network.NetworkStatusLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

class ViewModelMovieDetails @Inject constructor(
    private val apiService: ApiService,
    private val dbMovies: DbMovies,
    private val networkStatusLiveData: NetworkStatusLiveData,
) : BaseViewModel() {

    val liveDataMoviesDetails = MutableLiveData<DataDBMoviesDetails>()
    val liveDataMovieActors = MutableLiveData<List<ActorsInfo>>()

    fun loadMovieIdDetails(id: Long) {
        networkStatusLiveData.isNetworkAvailable().let { online ->
            if (online) {
                loadMovieDetailInServer(id)
            } else {
                loadMovieDetailInDb(id)
            }
        }
    }

    override fun subscriberLiveDataError(): LiveData<String> = liveDataError

    private fun loadMovieDetailInServer(id: Long) {
        scope.launch {
            val movieDetails =
                apiService.getMovieByIdAsync(id)
            if (movieDetails.isSuccessful) {
                movieDetails.body()?.let {
                    liveDataMoviesDetails.postValue(it.getMovieDetails())
                    dbMovies.moviesDetails().insertMovieDetail(it.getMovieDetails())
                    loadMoviesActors(id)
                }
            } else {
                loadMovieDetailInDb(id)
            }
        }
    }

    private fun loadMovieDetailInDb(id: Long) {
        scope.launch {
            loadActorsInDb(id)
            val resultDbDetails = dbMovies.moviesDetails().getMovieDetail(id)
            resultDbDetails?.let { liveDataMoviesDetails.postValue(it) }
        }
    }

    private fun loadMoviesActors(id: Long) {
        networkStatusLiveData.isNetworkAvailable().let { online ->
            if (online) {
                loadActorsInServer(id)
            } else {
                loadActorsInDb(id)
            }
        }
    }

    private fun loadActorsInServer(id: Long) {
        scope.launch {
            val movieActors =
                apiService.getMovieActorsCoroutineAsync(id)
            if (movieActors.isSuccessful) {
                movieActors.body()?.let { MovieActors ->
                    liveDataMovieActors.postValue(getListActor(MovieActors.cast))
                    loadActorsInDb(MovieActors, id)
                }
            } else {
                loadActorsInDb(id)
            }
        }
    }

    private fun loadActorsInDb(MovieActors: MovieActors, id: Long) {
        scope.launch {
            dbMovies.moviesDetails()
                .setNameActors(MovieActors.cast.joinToString(SEPARATOR) { it.name }, id)
            dbMovies.moviesDetails()
                .setProfilePaths(MovieActors.cast.joinToString(SEPARATOR) {
                    it.profilePath ?: " "
                }, id)
        }
    }

    private fun loadActorsInDb(id: Long) {
        scope.launch {
            val listActorsInfo = mutableListOf<ActorsInfo>()
            val resultDB = dbMovies.moviesDetails().getNameActors(id)
            if (!resultDB.isNullOrEmpty()) {
                val nameActors = resultDB.split(SEPARATOR)
                val listProfilePath =
                    dbMovies.moviesDetails().getProfilePaths(id).split(SEPARATOR)
                nameActors.forEachIndexed { index, _ ->
                    listActorsInfo.add(ActorsInfo(nameActors[index], listProfilePath[index]))
                }
                liveDataMovieActors.postValue(listActorsInfo)
            }
        }
    }

    override fun handleError(error: Throwable) {
        liveDataError.postValue(error.message)
    }
}