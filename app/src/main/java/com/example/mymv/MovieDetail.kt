package com.example.mymv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieDetail : AppCompatActivity() {

    private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_movie)


        val title_movie : TextView = findViewById(R.id.movie_title_details)
        val poster_movie : ImageView = findViewById(R.id.detail_poster)
        val backdrop_movie : ImageView = findViewById(R.id.backdrop_path)
        val release_date : TextView = findViewById(R.id.movie_release_date)
//        val movie_popularity : TextView = findViewById(R.id.movie_popularity)
        val movie_overview : TextView = findViewById(R.id.movie_overview)


        title_movie.text = intent.getStringExtra("title_movie")
        Glide.with(this).load(IMAGE_BASE + intent.getStringExtra("poster_movie")).into(poster_movie)
        Glide.with(this).load(IMAGE_BASE + intent.getStringExtra("backdrop_path")).into(backdrop_movie)
        release_date.text = intent.getStringExtra("movie_release")
//        movie_popularity.text = intent.getStringExtra("movie_popularity")
        movie_overview.text = intent.getStringExtra("movie_overview")



    }
}