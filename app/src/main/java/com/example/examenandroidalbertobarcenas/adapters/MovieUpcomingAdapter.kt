package com.example.examenandroidalbertobarcenas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.examenandroidalbertobarcenas.R
import com.example.examenandroidalbertobarcenas.factorys.NetworkState
import com.example.examenandroidalbertobarcenas.factorys.Status
import com.example.examenandroidalbertobarcenas.models.DataResults
import com.example.examenandroidalbertobarcenas.network.DataAccessible.POSTER_BASE_URL
import kotlinx.android.synthetic.main.network_state_item.view.*
import kotlinx.android.synthetic.main.movie_list_item.view.*

class MovieUpcomingAdapter (public val context: Context) :
    PagedListAdapter<DataResults, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val DATA_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == DATA_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            DATA_VIEW_TYPE
        }
    }

    class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: DataResults?, context:Context) {
            itemView.tvTitle.text = movie?.title

            itemView.tvDate.text =  movie?.releaseDate
            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                    .load(moviePosterURL)
                    .into(itemView.ivPoster);
        }
    }


    class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState.status == Status.RUNNING) {
                itemView.pbLoad.visibility = View.VISIBLE;
            } else {
                itemView.pbLoad.visibility = View.GONE;
            }

            if (networkState != null && networkState.status == Status.FAILED) {
                itemView.tvErrorMessage.visibility = View.VISIBLE;
                itemView.tvErrorMessage.setText(networkState.msg);
            } else {
                itemView.tvErrorMessage.visibility = View.GONE;
            }

        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<DataResults>() {
        override fun areItemsTheSame(oldItem: DataResults, newItem: DataResults): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataResults, newItem: DataResults): Boolean {
            return oldItem == newItem
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}