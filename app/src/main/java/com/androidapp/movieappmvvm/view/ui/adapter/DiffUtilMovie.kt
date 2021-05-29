package com.androidapp.movieappmvvm.view.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies

class DiffUtilMovie(
    private val oldList: List<DataDBMovies>,
    private val newList: List<DataDBMovies>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition].likeMovie == newList[newItemPosition].likeMovie
    }
}