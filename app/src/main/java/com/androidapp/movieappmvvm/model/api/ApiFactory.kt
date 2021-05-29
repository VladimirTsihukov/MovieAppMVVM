package com.androidapp.movieappmvvm.model.api

import android.annotation.SuppressLint
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object ApiFactory {
    private const val BASE_URL_MOVIE_COR = "https://api.themoviedb.org/3/movie/"
    private const val API_KEY_ID_COR = "7d9db2e12493542315f5bcb0f3f0de61"
    private const val API_KEY_COR = "api_key"
    private const val QUERY_PARAM_LANGUAGE_COR = "language"
    private const val LANGUAGE_RU = "ru"
    private const val LANGUAGE_ENG = "US"
    @SuppressLint("ConstantLocale")
    private val LANGUAGE_COR = if (Locale.getDefault().language == "ru") LANGUAGE_RU else LANGUAGE_ENG
    const val BASE_URL_MOVIE_IMAGE = "https://image.tmdb.org/t/p/w500/"

    private val authInterceptor: Interceptor = Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter(API_KEY_COR, API_KEY_ID_COR)
            .addQueryParameter(QUERY_PARAM_LANGUAGE_COR, LANGUAGE_COR)
            .build()

        val newRequest: Request = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val tmdClient: OkHttpClient = OkHttpClient()
        .newBuilder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private fun retrofitMovieCor(): Retrofit = Retrofit.Builder()
        .client(tmdClient)
        .baseUrl(BASE_URL_MOVIE_COR)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiServiceMovie: ApiService =
        retrofitMovieCor().create(ApiService::class.java)
}