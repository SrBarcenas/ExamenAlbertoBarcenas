package com.example.examenandroidalbertobarcenas.factorys

import androidx.lifecycle.MutableLiveData
import com.example.examenandroidalbertobarcenas.models.DataResults
import com.example.examenandroidalbertobarcenas.network.services.MoviesUpcomingApiClient
import io.reactivex.disposables.CompositeDisposable
import androidx.paging.DataSource

class MoviesUpcomingFactory(private val apiService : MoviesUpcomingApiClient, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, DataResults>() {

    val moviesLiveDataSource =  MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, DataResults> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}