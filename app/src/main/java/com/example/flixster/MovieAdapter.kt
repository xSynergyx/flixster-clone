package com.example.flixster

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.security.AccessController.getContext

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

    inner class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        private val ivPoster = itemView.findViewById<ImageView>(R.id.iv_poster)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tv_overview)

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
                .into(ivPoster)
        }
    }
}
