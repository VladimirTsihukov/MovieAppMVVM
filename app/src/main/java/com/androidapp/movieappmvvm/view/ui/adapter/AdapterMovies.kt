package com.androidapp.movieappmvvm.view.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.androidapp.movieappmvvm.R
import com.androidapp.movieappmvvm.data.dataDb.DataDBMovies
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_stars_holder.view.*
import kotlinx.android.synthetic.main.view_item_holder_movies.view.*
import kotlin.math.roundToInt

class AdapterMovies(
    private val onItemClickListener: OnItemClickListener,
) : RecyclerView.Adapter<AdapterMovies.HolderMovies>() {

    var movies: List<DataDBMovies> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMovies {
        return HolderMovies(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_item_holder_movies, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HolderMovies, position: Int) {
        holder.itemView.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.alpha)
        holder.onBind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class HolderMovies(item: View) : RecyclerView.ViewHolder(item) {
        private val imageFilm: ImageView = item.findViewById(R.id.img_holder_film)
        private val iconLike: ImageView = item.findViewById(R.id.img_holder_like)

        init {
            iconLike.setOnClickListener {
                onItemClickListener.onClickLikeMovies(movies[adapterPosition].likeMovie, movies[adapterPosition])
                movies[adapterPosition].likeMovie = !movies[adapterPosition].likeMovie
                notifyDataSetChanged()
            }
            imageFilm.setOnClickListener {
                onItemClickListener.onItemClick(movies[adapterPosition].id)
            }
        }

        fun onBind(movie: DataDBMovies) {
            with(itemView) {
                tv_holder_reviews.text = context.resources.getString(R.string.fragment_reviews).let {
                    String.format(it, "${movie.numberOfRatings}")
                }
                tv_holder_age_category.visibility = View.VISIBLE
                tv_holder_age_category.text = context.getString(R.string.fragment_age_category).let {
                    String.format(it, "${movie.minimumAge}")
                }
                tv_holder_film_name.text = movie.title
                img_mask.clipToOutline = true
            }
            imageFilm.clipToOutline = true
            setPosterIcon(movie.posterPath)
            setImageStars((movie.ratings / 2).roundToInt())
            iconLike.setImageResource(if (movie.likeMovie) R.drawable.ic_like_off
            else R.drawable.ic_like_on)
        }


        private fun setPosterIcon(poster: String) {
            Glide.with(itemView.context)
                .load(poster)
                .placeholder(R.drawable.ic_ph_movie_grey_200)
                .error(R.drawable.ic_ph_movie_grey_200)
                .into(imageFilm)
        }

        private fun setImageStars(current: Int) {
            itemView.apply {
                val listStar = listOf<ImageView>(
                    img_holder_star_level_1,
                    img_holder_star_level_2,
                    img_holder_star_level_3,
                    img_holder_star_level_4,
                    img_holder_star_level_5)

                listStar.forEachIndexed { index, _ ->
                    if (index < current) {
                        (listStar[index] as? ImageView)?.setImageResource(R.drawable.ic_star_on)
                    } else {
                        (listStar[index] as? ImageView)?.setImageResource(R.drawable.ic_star_off)
                    }
                }
            }
        }
    }
}

