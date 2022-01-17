package com.example.examenandroidalbertobarcenas.factorys

import androidx.paging.PageKeyedDataSource
import androidx.lifecycle.MutableLiveData
import com.example.examenandroidalbertobarcenas.models.DataResults
import com.example.examenandroidalbertobarcenas.network.FIRST_PAGE
import com.example.examenandroidalbertobarcenas.network.services.MoviesUpcomingApiClient
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MovieDataSource (private val apiService : MoviesUpcomingApiClient, private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, DataResults>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, DataResults>) {

        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getMovieUpcoming(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.results, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState(Status.FAILED, "Something went wrong"))
                    }
                )
        )

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataResults>) {

        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getMovieUpcoming(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages!! >= params.key){
                            callback.onResult(it.results, params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else{
                            networkState.postValue(NetworkState(Status.FAILED, "You have reached the end"))
                        }

                    },
                    {
                        networkState.postValue(NetworkState(Status.FAILED, "Something went wrong"))
                    }
                )
        )

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataResults>) {

    }
}
