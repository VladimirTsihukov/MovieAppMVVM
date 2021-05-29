package com.androidapp.movieappmvvm.view.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.androidapp.movieappmvvm.App
import com.androidapp.movieappmvvm.data.EnumTypeMovie
import com.androidapp.movieappmvvm.data.dataApi.parsInDataDBMoviesList
import com.androidapp.movieappmvvm.model.api.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkerCacheDBMovies(context: Context, workerParam: WorkerParameters) :
    CoroutineWorker(context, workerParam) {
    private val db by lazy { App.dbMovies }

    override suspend fun doWork(): Result {
        try {
            withContext(Dispatchers.IO) {
                val listMovies = ApiFactory.apiServiceMovie
                    .getMoviePopularAsync().body()?.results ?: listOf()

                if (listMovies.isNotEmpty()) {
                    db.movies().insertMovies(
                        parsInDataDBMoviesList(
                            EnumTypeMovie.POPULAR.name,
                        listMovies)
                    )
                }
            }
        } catch (error: Throwable) {
            Result.failure()
        }
        return Result.success()
    }
}
