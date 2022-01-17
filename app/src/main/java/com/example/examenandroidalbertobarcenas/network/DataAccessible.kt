package com.example.examenandroidalbertobarcenas.network

import com.example.examenandroidalbertobarcenas.network.services.MoviesUpcomingApiClient
import com.example.examenandroidalbertobarcenas.utils.ConnectivityInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20

object DataAccessible {

    val API_KEY = "64c68a6f96a20e3c95346f3a314a2944"
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

    fun getClient(connectivityInterceptor: ConnectivityInterceptor): MoviesUpcomingApiClient {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesUpcomingApiClient::class.java)
    }
}