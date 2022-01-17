package com.example.examenandroidalbertobarcenas.adapters

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import com.example.examenandroidalbertobarcenas.databinding.LayoutItemBinding
import com.example.examenandroidalbertobarcenas.R
import java.util.ArrayList

class ImageAdapter(val imageList: ArrayList<Any>?, val context: Context?): RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding: LayoutItemBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.layout_item,
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context).load(imageList!![position]).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList!!.size
    }

    class ViewHolder(val binding: LayoutItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var imageView: ImageView

        init {
            imageView = binding.item
        }
    }
}