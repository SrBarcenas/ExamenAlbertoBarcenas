package com.example.examenandroidalbertobarcenas.network.services

import com.example.examenandroidalbertobarcenas.models.BaseResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesUpcomingApiClient {

    @GET("movie/upcoming")
    fun getMovieUpcoming(
        @Query("page") page: Int
    ): Single<BaseResponse>
}