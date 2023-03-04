package com.androidapp.movieappmvvm.api

import com.androidapp.movieappmvvm.api.dataApi.MovieActors
import com.androidapp.movieappmvvm.api.dataApi.MoviesDetail
import com.androidapp.movieappmvvm.api.dataApi.MoviesList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("popular")
    suspend fun getMoviePopularAsync(
    ): Response<MoviesList>

    @GET("top_rated")
    suspend fun getMovieTopRatedAsync(
    ): Response<MoviesList>

    @GET("{movie_id}")
    suspend fun getMovieByIdAsync(
        @Path(QUERY_PARAM_MOVIE_ID_COR) movieID: Long,
    ): Response<MoviesDetail>

    @GET("{movie_id}/credits")
    suspend fun getMovieActorsCoroutineAsync(
        @Path(QUERY_PARAM_MOVIE_ID_COR) movieID: Long,
    ): Response<MovieActors>

    companion object {
        private const val QUERY_PARAM_MOVIE_ID_COR = "movie_id"
    }
}
