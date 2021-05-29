package com.androidapp.movieappmvvm.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.movieappmvvm.R
import com.androidapp.movieappmvvm.data.EnumTypeMovie
import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies
import com.androidapp.movieappmvvm.view.ui.adapter.AdapterMovies
import com.androidapp.movieappmvvm.view.ui.adapter.OnItemClickListener
import com.androidapp.movieappmvvm.view.ui.viewModel.ViewModelMovieList
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

const val MOVIES_KEY = "MOVIES"

class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {

    private val mViewModelMovieList: ViewModelMovieList by viewModels()

    private lateinit var recycler: RecyclerView
    private lateinit var adapterMovies: AdapterMovies
    private lateinit var mShimmerContainer: ShimmerFrameLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShimmerContainer(view)
        initBottomNavigation()
        initRecycler(view)
        initLiveData(view)
    }

    private fun initShimmerContainer(view: View) {
        mShimmerContainer = view.findViewById(R.id.shimmer_view_container)
        startAnimateShimmer()
    }

    private fun initBottomNavigation() {
        activity?.let {
            it.bottom_navigation.setOnNavigationItemSelectedListener { item ->
                startAnimateShimmer()
                when (item.itemId) {
                    R.id.nav_popular -> {
                        mViewModelMovieList.loadMoviesMovies(EnumTypeMovie.POPULAR)
                        true
                    }
                    R.id.nav_top -> {
                        mViewModelMovieList.loadMoviesMovies(EnumTypeMovie.TOP)
                        true
                    }
                    R.id.nav_favorite -> {
                        mViewModelMovieList.loadMoviesMovies(EnumTypeMovie.FAVORITE)
                        true
                    }
                    else -> {
                        true
                    }
                }
            }
        }
    }

    private fun initRecycler(view: View) {
        recycler = view.findViewById(R.id.res_view_move_list)
        adapterMovies = AdapterMovies(click)
        recycler.adapter = adapterMovies
    }

    private fun initLiveData(view: View) {
        mViewModelMovieList.liveDataMoviesList.observe(viewLifecycleOwner,
            { movie ->
                updateData(movie)
            })

        mViewModelMovieList.subscriberLiveDataError().observe(viewLifecycleOwner, {
            activity?.let { fragmentActivity ->
                Snackbar.make(
                    view,
                    fragmentActivity.getString(R.string.error_server_connect),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateData(list: List<DataDBMovies>?) {
        stopAnimateShimmer()

        list?.let {
            adapterMovies.movies = it
        }
    }

    private fun startAnimateShimmer() {
        mShimmerContainer.visibility = View.VISIBLE
        mShimmerContainer.startShimmerAnimation()
    }

    private fun stopAnimateShimmer() {
        mShimmerContainer.stopShimmerAnimation()
        mShimmerContainer.visibility = View.GONE
    }

    private val click = object : OnItemClickListener {
        override fun onItemClick(id: Long) {
            val bundle = Bundle()
            bundle.putLong(MOVIES_KEY, id)
            findNavController().navigate(
                R.id.action_fragmentMoviesList_to_fragmentMoviesDetails,
                bundle
            )
        }

        override fun onClickLikeMovies(iconLike: Boolean, movie: DataDBMovies) {
            if (iconLike) {
                mViewModelMovieList.deleteMoviesLikeInDb(movie)
            } else {
                mViewModelMovieList.addMovieLikeInDb(movie)
            }
        }
    }
}

