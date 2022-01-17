package com.example.examenandroidalbertobarcenas.fragments.viewmodel

import androidx.lifecycle.ViewModel;
import com.example.examenandroidalbertobarcenas.repositorys.MoviesUpcomingRepository
import io.reactivex.disposables.CompositeDisposable


class StartViewModel (private val movieRepository : MoviesUpcomingRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  moviePagedList by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val  networkState by lazy {
        movieRepository.getNetworkState()
    }


    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}
