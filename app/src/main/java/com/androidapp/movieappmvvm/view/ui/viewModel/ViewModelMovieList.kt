package com.androidapp.movieappmvvm.view.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import com.androidapp.movieappmvvm.App
import com.androidapp.movieappmvvm.data.EnumTypeMovie
import com.androidapp.movieappmvvm.data.dataApi.Movie
import com.androidapp.movieappmvvm.data.dataApi.MoviesList
import com.androidapp.movieappmvvm.data.dataApi.parsInDataDBMovies
import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies
import com.androidapp.movieappmvvm.data.dataDb.parsInDataDBMoviesLike
import com.androidapp.movieappmvvm.data.dataDb.parsInDataDataDBMovies
import com.androidapp.movieappmvvm.model.api.ApiFactory
import com.androidapp.movieappmvvm.view.service.WorkerCacheDBMovies
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.concurrent.TimeUnit

class ViewModelMovieList : BaseViewModel() {

    private var flagMovieFavorite = false
    val liveDataMoviesList = MutableLiveData<List<DataDBMovies>>()

    init {
        loadMoviesMovies(EnumTypeMovie.POPULAR)
        startWorkManager()
    }

    override fun subscriberLiveDataError(): LiveData<String> = liveDataError

    fun loadMoviesMovies(typeMovie: EnumTypeMovie) {
        flagMovieFavorite = typeMovie.name == EnumTypeMovie.FAVORITE.name
        if (flagMovieFavorite) {
            getMoviesListLikeForDb()
        } else {
            App.networkStatusLiveData.isNetworkAvailable().let { online ->
                if (online) {
                    getMovieInServer(typeMovie)
                } else {
                    getMoviesInDb(typeMovie)
                }
            }
        }
    }

    private fun getMoviesListLikeForDb() {
        scope.launch {
            val moviesLike = dbMovies.moviesLike().getMoviesLike()
            liveDataMoviesList.postValue(parsInDataDataDBMovies(moviesLike))
        }
    }

    private fun startWorkManager() {
        val const = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val constrainRequest =
            PeriodicWorkRequest.Builder(WorkerCacheDBMovies::class.java, 8, TimeUnit.HOURS)
                .setConstraints(const)
                .build()

        WorkManager.getInstance(App.instance)
            .enqueueUniquePeriodicWork(
                "Update DB",
                ExistingPeriodicWorkPolicy.KEEP,
                constrainRequest
            )
    }

    fun addMovieLikeInDb(movie: DataDBMovies) {
        scope.launch {
            try {
                dbMovies.moviesLike().insetMoviesLike(movie.parsInDataDBMoviesLike())
                dbMovies.movies().setMoviesIdLikeInDb(movie.id, movie.likeMovie)
            } catch (e: Exception) {
                liveDataError.postValue(e.message)
            }
        }
    }

    fun deleteMoviesLikeInDb(movie: DataDBMovies) {
        scope.launch {
            try {
                dbMovies.moviesLike().deleteMoviesLike(movie.id)
                dbMovies.movies().setMoviesIdLikeInDb(movie.id, movie.likeMovie)
                if (flagMovieFavorite) {
                    liveDataMoviesList.postValue(parsInDataDataDBMovies(dbMovies.moviesLike().getMoviesLike()))
                }
            } catch (e: Exception) {
                liveDataError.postValue(e.message)
            }
        }
    }

    private fun getMoviesInDb(typeMovie: EnumTypeMovie) {
        scope.launch {
            when (typeMovie.name) {
                EnumTypeMovie.POPULAR.name ->
                    liveDataMoviesList.postValue(dbMovies.movies().getMoviesCategory(typeMovie.name))
                EnumTypeMovie.TOP.name ->
                    liveDataMoviesList.postValue(dbMovies.movies().getMoviesCategory(typeMovie.name))
            }
        }
    }

    private fun getMovieInServer(typeMovie: EnumTypeMovie) {
        scope.launch {
            when (typeMovie.name) {
                EnumTypeMovie.TOP.name ->
                    errorHandling(ApiFactory.apiServiceMovie.getMovieTopRatedAsync(), typeMovie)
                EnumTypeMovie.POPULAR.name ->
                    errorHandling(ApiFactory.apiServiceMovie.getMoviePopularAsync(), typeMovie)
            }
        }
    }

    private fun errorHandling(movies: Response<MoviesList>, typeMovie: EnumTypeMovie) {
        if (movies.isSuccessful) {
            movies.body()?.let { Movies ->
                processResultDataInApi(Movies.results, typeMovie)
            }
        } else {
            getMoviesInDb(typeMovie)
        }
    }

    private fun processResultDataInApi(listMovies: List<Movie>, typeMovie: EnumTypeMovie) {
        scope.launch {
            val movieListResult = mutableListOf<DataDBMovies>()
            val listIdMovieLike = dbMovies.moviesLike().getAllID()
            listMovies.forEach {
                it.typeMovie = typeMovie.name
                if (listIdMovieLike.contains(it.id)) {
                    it.likeMovie = true
                }
                movieListResult.add(it.parsInDataDBMovies())
            }
            dbMovies.movies().insertMovies(movieListResult)
            liveDataMoviesList.postValue(movieListResult)
        }
    }

    override fun handleError(error: Throwable) {
        liveDataError.postValue(error.message)
    }
}