package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers
import org.json.JSONException
import org.w3c.dom.Text

private const val YOUTUBE_API_KEY = "AIzaSyCB6F5KEb6L5fJbtV43wiFo0BjKZFBzq4U"
private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=04240126137c074c41f98fa14ebdce47&language=en-US"
private const val TAG = "DetailActivity"
class DetailActivity : YouTubeBaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var ytPlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        tvTitle = findViewById(R.id.tv_title)
        tvOverview = findViewById(R.id.tv_overview)
        ratingBar = findViewById(R.id.rb_vote_average)
        ytPlayerView = findViewById(R.id.player)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie // as movie because we don't want it to be nullable
        Log.i(TAG, "Movie is $movie")
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingBar.rating = movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object: JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")
                val results = json.jsonObject.getJSONArray("results")
                if (results.length() == 0) {
                    Log.w(TAG, "No movie trailers found")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")
                initializeYoutube(youtubeKey)
            }

        })
    }

    private fun initializeYoutube(youtubeKey: String) {

        ytPlayerView.initialize(YOUTUBE_API_KEY, object: YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializationSuccess")
                player?.cueVideo(youtubeKey)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i(TAG, "onInitializationFailure $p1")
            }

        })
    }
}