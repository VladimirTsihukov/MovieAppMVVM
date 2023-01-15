package com.androidapp.movieappmvvm.view.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.androidapp.movieappmvvm.data.EnumTypeMovie
import com.androidapp.movieappmvvm.data.dataApi.parsInDataDBMoviesList
import com.androidapp.movieappmvvm.di.components.appComponent
import com.androidapp.movieappmvvm.model.api.ApiService
import com.androidapp.movieappmvvm.model.database.databaseMoviesList.DbMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkerCacheDBMovies(context: Context, workerParam: WorkerParameters) :
    CoroutineWorker(context, workerParam) {

    private val apiService: ApiService = context.appComponent.getApiService()
    private val dbMovies: DbMovies = context.appComponent.getDbMovies()

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
}
