package com.androidapp.movieappmvvm.view.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {
    protected val liveDataError = MutableLiveData<String>()
    protected val scope = CoroutineScope(
        Dispatchers.IO
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })

    abstract fun handleError(error: Throwable)

    abstract fun subscriberLiveDataError(): LiveData<String>

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}