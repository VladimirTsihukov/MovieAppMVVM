package com.androidapp.movieappmvvm.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.androidapp.movieappmvvm.api.ApiService
import com.androidapp.movieappmvvm.api.dataApi.Movie
import com.androidapp.movieappmvvm.data.EnumTypeMovie
import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies
import com.androidapp.movieappmvvm.data.database.databaseMoviesList.DbMovies
import com.androidapp.movieappmvvm.di.components.appComponent
import com.androidapp.movieappmvvm.utils.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkerCacheDBMovies(context: Context, workerParam: WorkerParameters) :
    CoroutineWorker(context, workerParam) {

    private val apiService: ApiService = context.appComponent.apiService
    private val dbMovies: DbMovies = context.appComponent.dbMovies

    override suspend fun doWork(): Result {
        try {
            withContext(Dispatchers.IO) {
                val listMovies = apiService.getMoviePopularAsync().body()?.results ?: listOf()

                if (listMovies.isNotEmpty()) {
                    dbMovies.movies().insertMovies(
                        parsInDataDBMoviesList(EnumTypeMovie.POPULAR.name, listMovies)
                    )
                }
            }
        } catch (error: Throwable) {
            Result.failure()
        }
        return Result.success()
    }

    private fun parsInDataDBMoviesList(moviePopular: String, listMovie: List<Movie>): List<DataDBMovies> {
        val listDataDBMovies = mutableListOf<DataDBMovies>()
        listMovie.forEach {
            listDataDBMovies.add(
                DataDBMovies(
                    id = it.id,
                    title = it.title,
                    overview = it.overview,
                    posterPath = ApiUtils.BASE_URL_MOVIE_IMAGE + it.posterPath,
                    backdropPath = ApiUtils.BASE_URL_MOVIE_IMAGE + it.backdropPath,
                    ratings = it.voteAverage,
                    numberOfRatings = it.voteCount,
                    minimumAge = if (it.adult) 16 else 13,
                    likeMovie = it.likeMovie,
                    typeMovie = moviePopular
                )
            )
        }
        return listDataDBMovies
    }
}
