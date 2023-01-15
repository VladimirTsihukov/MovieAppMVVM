package com.androidapp.movieappmvvm.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider as XViewModelProvider

interface ViewModelProvider<V : ViewModel> {
    fun getViewModel(): V
}

inline fun <reified P, reified V> viewModelFactory(provider: P): XViewModelProvider.Factory
        where P : ViewModelProvider<V>,
              V : ViewModel {
    return object : XViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = provider.getViewModel() as T
    }
}

