package com.androidapp.moviesappmvvm.feature.movies_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider as XViewModelProvider

interface CustomViewModelProvider<V : ViewModel> {
    fun getViewModel(): V
}

inline fun <reified P, reified V> customViewModelFactory(provider: P): XViewModelProvider.Factory
        where P : CustomViewModelProvider<V>,
              V : ViewModel {
    return object : XViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = provider.getViewModel() as T
    }
}

