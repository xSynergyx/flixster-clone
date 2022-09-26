package com.example.flixster

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import java.security.AccessController.getContext
import androidx.core.util.Pair as Pair

const val MOVIE_EXTRA = "MOVIE_EXTRA"
class MovieAdapter(private val context: Context, private val movies: List<Movie>)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]

        holder.bind(movie)
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val ivPoster = itemView.findViewById<ImageView>(R.id.iv_poster)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tv_overview)
        private val overlay = itemView.findViewById<ImageView>(R.id.play_overlay)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie){

            tvTitle.text = movie.title
            tvOverview.text = movie.overview

            var image = movie.posterImageUrl
            val orientation = context.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                image = movie.backdropImageUrl
            }

            Glide
                .with(context)
                .load(image)
                .transform(RoundedCorners(20))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: Boolean): Boolean {
                        Log.e("MovieAdapter", "onLoadFailed")
                        //do something if error loading
                        return false
                    }
                    // TODO: finish the play overlay
                    override fun onResourceReady(p0: Drawable?, p1: Any?, p2: com.bumptech.glide.request.target.Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {
                        Log.d("MovieAdapter", "OnResourceReady")
                        if (movie.voteAverage.toFloat() > 7) {
                            overlay.visibility = VISIBLE
                        }
                        return false
                    }
                })
                .into(ivPoster)
        }

        override fun onClick(v: View?) {
            val movie = movies[adapterPosition]

            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA, movie)
            val pair1 = Pair.create<View?, String?>(tvTitle, "movie_title_transition")
            val pair2 = Pair.create<View?, String?>(tvOverview, "movie_description_transition")
            val pair3 = Pair.create<View?, String?>(ivPoster, "movie_image_transition")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, pair1, pair2, pair3)
            context.startActivity(intent, options.toBundle())
        }
    }
}
