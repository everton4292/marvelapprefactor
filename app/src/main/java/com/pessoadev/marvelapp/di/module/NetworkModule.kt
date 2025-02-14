package com.pessoadev.marvelapp.di.module

import com.pessoadev.marvelapp.BuildConfig
import com.pessoadev.marvelapp.data.repository.MarvelRepositoryImp
import com.pessoadev.marvelapp.data.source.local.CharacterDao
import com.pessoadev.marvelapp.data.source.remote.ApiService
import com.pessoadev.marvelapp.domain.repository.MarvelRepository
import com.pessoadev.marvelapp.util.HashGenerate
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val NetworkModule = module {

    single { createService(get()) }

    single { createRetrofit(get(), BuildConfig.BASE_URL) }

    single { createOkHttpClient() }

    single { createMarvelRepository(get(),get())}
}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
    return OkHttpClient.Builder()
        .connectTimeout(60L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor { chain -> createParametersDefault(chain) }
        .addInterceptor(httpLoggingInterceptor).build()

}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun createParametersDefault(chain: Interceptor.Chain): Response {
    val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
    var request = chain.request()
    val builder = request.url().newBuilder()

    builder.addQueryParameter("apikey", BuildConfig.API_PUBLIC)
        .addQueryParameter("hash", HashGenerate.generate(timeStamp, BuildConfig.API_PRIVATE, BuildConfig.API_PUBLIC))
        .addQueryParameter("ts", timeStamp.toString())

    request = request.newBuilder().url(builder.build()).build()
    return chain.proceed(request)
}

fun createService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun createMarvelRepository(apiService: ApiService, characterDao: CharacterDao): MarvelRepository {
    return MarvelRepositoryImp(apiService,characterDao)
}
