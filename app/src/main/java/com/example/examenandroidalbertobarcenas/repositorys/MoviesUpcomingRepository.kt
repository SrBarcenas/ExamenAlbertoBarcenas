package com.example.examenandroidalbertobarcenas.repositorys

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.examenandroidalbertobarcenas.factorys.MovieDataSource
import com.example.examenandroidalbertobarcenas.factorys.MoviesUpcomingFactory
import com.example.examenandroidalbertobarcenas.factorys.NetworkState
import com.example.examenandroidalbertobarcenas.models.DataResults
import com.example.examenandroidalbertobarcenas.network.POST_PER_PAGE
import com.example.examenandroidalbertobarcenas.network.services.MoviesUpcomingApiClient
import io.reactivex.disposables.CompositeDisposable

class MoviesUpcomingRepository (private val apiService : MoviesUpcomingApiClient) {

    lateinit var moviePagedList: LiveData<PagedList<DataResults>>
    lateinit var moviesDataSourceFactory: MoviesUpcomingFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<DataResults>> {
        moviesDataSourceFactory =
            MoviesUpcomingFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedList

    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState)
    }
}