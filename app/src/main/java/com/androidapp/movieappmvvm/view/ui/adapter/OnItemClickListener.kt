package com.androidapp.movieappmvvm.view.ui.adapter

import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies

interface OnItemClickListener {
    fun onItemClick (id: Long)
    fun onClickLikeMovies(iconLike: Boolean, movie: DataDBMovies)
}