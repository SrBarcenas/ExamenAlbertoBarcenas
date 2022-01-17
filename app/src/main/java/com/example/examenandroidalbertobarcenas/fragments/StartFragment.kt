package com.example.examenandroidalbertobarcenas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.examenandroidalbertobarcenas.R
import com.example.examenandroidalbertobarcenas.adapters.MovieUpcomingAdapter
import com.example.examenandroidalbertobarcenas.factorys.NetworkState
import com.example.examenandroidalbertobarcenas.factorys.Status
import com.example.examenandroidalbertobarcenas.fragments.viewmodel.StartViewModel
import com.example.examenandroidalbertobarcenas.network.DataAccessible
import com.example.examenandroidalbertobarcenas.network.services.MoviesUpcomingApiClient
import com.example.examenandroidalbertobarcenas.repositorys.MoviesUpcomingRepository
import com.example.examenandroidalbertobarcenas.utils.ConnectivityInterceptor
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : Fragment() {

    companion object {
        fun newInstance() = StartFragment()
    }

    private lateinit var viewModel: StartViewModel

    lateinit var movieRepository: MoviesUpcomingRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val apiService: MoviesUpcomingApiClient = DataAccessible.getClient(
            ConnectivityInterceptor(
                this.requireContext()
            )
        )
        movieRepository = MoviesUpcomingRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = MovieUpcomingAdapter(this.requireContext())

        val gridLayoutManager = GridLayoutManager(this.requireContext(), 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.DATA_VIEW_TYPE) return 1
                else return 3
            }
        };

        rvMovieList.layoutManager = gridLayoutManager
        rvMovieList.setHasFixedSize(true)
        rvMovieList.adapter = movieAdapter

        viewModel.moviePagedList.observe(viewLifecycleOwner, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            pbLoad.visibility =
                if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            tvError.visibility =
                if (viewModel.listIsEmpty() && it.status == Status.FAILED) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): StartViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return StartViewModel(movieRepository) as T
            }
        })[StartViewModel::class.java]
    }
}