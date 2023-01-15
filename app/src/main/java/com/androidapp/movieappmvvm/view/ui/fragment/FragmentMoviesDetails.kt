package com.androidapp.movieappmvvm.view.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.movieappmvvm.R
import com.androidapp.movieappmvvm.data.dataApi.ActorsInfo
import com.androidapp.movieappmvvm.data.dataDb.DataDBMoviesDetails
import com.androidapp.movieappmvvm.di.components.movieDetailsComponent
import com.androidapp.movieappmvvm.utils.viewModelFactory
import com.androidapp.movieappmvvm.view.ui.adapter.AdapterActors
import com.androidapp.movieappmvvm.view.ui.viewModel.ViewModelMovieDetails
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movies_details.*
import kotlinx.android.synthetic.main.layout_back_fragment.view.*
import kotlinx.android.synthetic.main.layout_stars.*
import kotlin.math.roundToInt

class FragmentMoviesDetails : Fragment(R.layout.fragment_movies_details) {

    private var viewModel: ViewModelMovieDetails? = null

    private lateinit var recyclerView: RecyclerView
    private val adapter = AdapterActors()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rec_view_actors)
        recyclerView.adapter = adapter

        arguments?.getLong(MOVIES_KEY)?.let {
            viewModel?.loadMovieIdDetails(it)
        }

        initView(view)
        initObserver(view)
    }

    private fun getViewModel(): ViewModelMovieDetails? {
        return movieDetailsComponent?.let {
            ViewModelProvider(this, viewModelFactory(it))[ViewModelMovieDetails::class.java]
        }
    }

    private fun initView(view: View) {
        view.tv_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObserver(view: View) {
        viewModel?.apply {
            liveDataMoviesDetails.observe(viewLifecycleOwner) { movieDetail ->
                movieDetail?.run {
                    getInitLayout(movieDetail)
                    hideLoader()
                }
            }
            liveDataMovieActors.observe(viewLifecycleOwner) { actors ->
                if (actors.isNullOrEmpty()) {
                    tv_mov_list_cast.visibility = View.INVISIBLE
                } else {
                    updateDataActors(actors)
                }
            }
            subscriberLiveDataError().observe(viewLifecycleOwner) {
                activity?.let {
                    Snackbar.make(
                        view,
                        resources.getString(R.string.error_server_connect),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun updateDataActors(movieActors: List<ActorsInfo>) {
        adapter.actors = movieActors
    }

    private fun getInitLayout(movieData: DataDBMoviesDetails) {
        view?.also {
            tv_mov_list_age_category.text =
                resources.getString(R.string.fragment_age_category).let {
                    String.format(it, "${movieData.minimumAge}")
                }
            tv_mov_list_movie_genre.text = movieData.genres
            tv_mov_list_text_story_line.text = movieData.overview
            tv_mov_list_reviews.text = resources.getString(R.string.fragment_reviews).let {
                String.format(it, "${movieData.numberOfRatings}")
            }
            tv_mov_list_film_name.text = movieData.title
            setPosterIcon(movieData.backdrop)
            setImageStars((movieData.ratings / 2).roundToInt())
        }
    }

    private fun hideLoader() {
        Handler(Looper.getMainLooper()).postDelayed({
            view?.let {
                data_loader.visibility = View.INVISIBLE
            }
        }, 1000)
    }

    private fun setPosterIcon(poster: String) {
        view?.apply {
            Glide.with(context)
                .load(poster)
                .error(R.drawable.ic_ph_movie_grey_400)
                .into(img_poster)
        }
    }

    private fun setImageStars(current: Int) {
        view?.apply {
            val listStar = listOf<ImageView>(
                img_mov_list_star_level_1,
                img_mov_list_star_level_2,
                img_mov_list_star_level_3,
                img_mov_list_star_level_4,
                img_mov_list_star_level_5
            )

            listStar.forEachIndexed { index, _ ->
                if (index < current) {
                    listStar[index].setImageResource(R.drawable.ic_star_on)
                } else {
                    listStar[index].setImageResource(R.drawable.ic_star_off)
                }
            }
        }
    }
}