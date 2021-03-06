package com.example.examenandroidalbertobarcenas.models

import com.google.gson.annotations.SerializedName

data class DataResults (
    @SerializedName("id")                   var id: Int,
    @SerializedName("poster_path")          var posterPath: String?,
    @SerializedName("release_date")         var releaseDate: String,
    @SerializedName("title")                var title: String
    )