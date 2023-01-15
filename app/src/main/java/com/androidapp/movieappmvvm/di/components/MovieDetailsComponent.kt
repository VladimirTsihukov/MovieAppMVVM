package com.androidapp.movieappmvvm.di.components

import androidx.fragment.app.Fragment
import com.androidapp.movieappmvvm.App
import com.androidapp.movieappmvvm.utils.ViewModelProvider
import com.androidapp.movieappmvvm.view.ui.viewModel.ViewModelMovieDetails
import dagger.Component
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MovieDetailsScope

@MovieDetailsScope
@Component(dependencies = [AppComponent::class])
interface MovieDetailsComponent : ViewModelProvider<ViewModelMovieDetails>

val Fragment.movieDetailsComponent: MovieDetailsComponent?
    get() = DaggerMovieDetailsComponent.builder()
        .appComponent((activity?.application as App).appComponent)
        .build()