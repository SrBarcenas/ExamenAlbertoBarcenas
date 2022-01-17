package com.example.examenandroidalbertobarcenas.factorys

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}


class NetworkState(val status: Status, val msg: String) {

    companion object {

        val LOADED: NetworkState
        val LOADING: NetworkState

        init {
            LOADED =
                NetworkState(
                    Status.SUCCESS,
                    "Success"
                )
            LOADING =
                NetworkState(
                    Status.RUNNING,
                    "Running"
                )

        }
    }

}