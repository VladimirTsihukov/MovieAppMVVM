package com.androidapp.movieappmvvm.view.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.movieappmvvm.R
import com.androidapp.movieappmvvm.data.EnumTypeMovie
import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies
import com.androidapp.movieappmvvm.di.components.movieListComponent
import com.androidapp.movieappmvvm.view.ui.adapter.AdapterMovies
import com.androidapp.movieappmvvm.view.ui.adapter.OnItemClickListener
import com.androidapp.movieappmvvm.view.ui.viewModel.ViewModelMovieList
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

const val MOVIES_KEY = "MOVIES"

class FragmentMoviesList : Fragment(R.layout.fragment_movies_list) {

    private var viewModel: ViewModelMovieList? = null

    private lateinit var recycler: RecyclerView
    private lateinit var adapterMovies: AdapterMovies
    private lateinit var mShimmerContainer: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModelList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShimmerContainer(view)
        initBottomNavigation()
        initRecycler(view)
        initLiveData(view)
    }

    private fun getViewModelList(): ViewModelMovieList? {
        val viewModelFactory = movieListComponent?.getViewModelFactory()
        return viewModelFactory?.let {
            ViewModelProvider(this@FragmentMoviesList, it).get(ViewModelMovieList::class.java)
        }
    }

    private fun initShimmerContainer(view: View) {
        mShimmerContainer = view.findViewById(R.id.shimmer_view_container)
        startAnimateShimmer()
    }

    private fun initBottomNavigation() {
        activity?.bottom_navigation?.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_popular -> {
                    viewModel?.loadMoviesMovies(EnumTypeMovie.POPULAR)
                    true
                }
                R.id.nav_top -> {
                    viewModel?.loadMoviesMovies(EnumTypeMovie.TOP)
                    true
                }
                R.id.nav_favorite -> {
                    viewModel?.loadMoviesMovies(EnumTypeMovie.FAVORITE)
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun initRecycler(view: View) {
        recycler = view.findViewById(R.id.res_view_move_list)
        recycler.apply {
            val spanCount = when (resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> 4
                else -> 2
            }
            layoutManager = GridLayoutManager(activity, spanCount)
        }
        adapterMovies = AdapterMovies(click)
        recycler.adapter = adapterMovies
    }

    private fun initLiveData(view: View) {
        viewModel?.apply {
            liveDataMoviesList.observe(viewLifecycleOwner) {
                updateData(it)
            }
            subscriberLiveDataError().observe(viewLifecycleOwner) {
                activity?.let { fragmentActivity ->
                    Snackbar.make(
                        view,
                        fragmentActivity.getString(R.string.error_server_connect),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateData(list: List<DataDBMovies>?) {
        stopAnimateShimmer()

        list?.let {
            adapterMovies.bindMovies(it)
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
                viewModel?.deleteMoviesLikeInDb(movie)
            } else {
                viewModel?.addMovieLikeInDb(movie)
            }
        }
    }
}

