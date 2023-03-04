package com.androidapp.moviesappmvvm.feature.movies_detail.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.androidapp.movieappmvvm.api.ApiService
import com.androidapp.movieappmvvm.api.NetworkStatusLiveData
import com.androidapp.movieappmvvm.api.dataApi.*
import com.androidapp.movieappmvvm.data.dataDb.DataDBMoviesDetails
import com.androidapp.movieappmvvm.data.database.DatabaseContact.SEPARATOR
import com.androidapp.movieappmvvm.data.database.databaseMoviesList.DbMovies
import com.androidapp.movieappmvvm.utils.ApiUtils.BASE_URL_MOVIE_IMAGE
import com.androidapp.moviesappmvvm.feature.movies_detail.BaseViewModel
import com.androidapp.moviesappmvvm.feature.movies_detail.di.MovieDetailsScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@MovieDetailsScope
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

internal fun MoviesDetail.getMovieDetails(): DataDBMoviesDetails {

    return this.run {
        @Suppress("unused")
        (DataDBMoviesDetails(
            id = id,
            title = title,
            overview = overview,
            backdrop = BASE_URL_MOVIE_IMAGE + backdropPath,
            ratings = ratings,
            numberOfRatings = voteCount.toInt(),
            minimumAge = if (adult) 16 else 13,
            runtime = runtime,
            genres = genres.joinToString(", ") { it.name },
        ))
    }
}