package com.androidapp.movieappmvvm.di.modules

import com.androidapp.movieappmvvm.di.components.AppScope
import com.androidapp.movieappmvvm.model.api.ApiService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class ApiModule {

    companion object {
        private const val LANGUAGE_RU = "ru"
        private const val LANGUAGE_ENG = "US"
        private const val QUERY_PARAM_LANGUAGE_COR = "language"
    }

    @AppScope
    @Provides
    @Named("baseUrlMovie")
    fun getBaseUrlMovie(): String = "https://api.themoviedb.org/3/movie/"

    @AppScope
    @Provides
    @Named("apiKey")
    fun getApiKey(): String = "api_key"

    @AppScope
    @Provides
    @Named("apiKeyId")
    fun getApiKeyId(): String = "7d9db2e12493542315f5bcb0f3f0de61"

    @AppScope
    @Provides
    @Named("languageCor")
    fun getLanguageCor(): String =
        if (Locale.getDefault().language == "ru") LANGUAGE_RU else LANGUAGE_ENG

    @AppScope
    @Provides
    fun getInterceptor(
        @Named("apiKey") apiKey: String,
        @Named("apiKeyId") apiKeyId: String,
        @Named("languageCor") languageCor: String,
    ): Interceptor {
        return Interceptor { chain ->
            val newUrl = chain.request().url
                .newBuilder()
                .addQueryParameter(apiKey, apiKeyId)
                .addQueryParameter(QUERY_PARAM_LANGUAGE_COR, languageCor)
                .build()

            val newRequest: Request = chain.request()
                .newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
    }

    @AppScope
    @Provides
    fun getOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @AppScope
    @Provides
    fun getRetrofit(
        tmdClient: OkHttpClient,
        @Named("baseUrlMovie") baseUrlMovie: String,
    ): Retrofit {
        return Retrofit.Builder()
            .client(tmdClient)
            .baseUrl(baseUrlMovie)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @AppScope
    @Provides
    fun getApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}