package com.example.mymv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymv.adapter.WatchlistAdapter
import com.example.mymv.adapter.homeAdapter.ListGenreAdapter
import com.example.mymv.models.GenreModel
import com.example.mymv.models.MovieModel
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.ListGenreMovieInterface
import com.example.mymv.services.RetrofitInstance
import kotlinx.android.synthetic.main.activity_list_genre_movie.*
import kotlinx.android.synthetic.main.fragment_home_fragments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListGenreMovie : AppCompatActivity() {
    private lateinit var genreName: TextView
    private lateinit var listGenreMovieAdapter: WatchlistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_genre_movie)

        genreName = findViewById(R.id.tvGenreName)

        val genreListName = intent.getStringExtra("genre_name")

        genreName.text = genreListName


        val mutableList = emptyList<MovieModel>().toMutableList()



        listGenreMovieAdapter = WatchlistAdapter(mutableList, object: WatchlistAdapter.OnAdapterListener{
            override fun onClick(movieModel: MovieModel) {
//
                Log.d("ErrorCheck", "This is ${movieModel.overview}")
//                startActivity(
//                    Intent(this@ListGenreMovie, ListGenreMovie::class.java)
//                    .putExtra("genre_id", genreModel.id)
//                    .putExtra("genre_name", genreModel.name)
//                )


            }

        })


        rvListGenreMovie.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvListGenreMovie.setHasFixedSize(true)
        getListGenreMovie{ movieModelsMutable : MutableList<MovieModel> ->
            rvListGenreMovie.adapter = WatchlistAdapter(movieModelsMutable, listGenreMovieAdapter.listener)
        }
    }



    private fun getListGenreMovie(callback: (MutableList<MovieModel>) -> Unit) {
        val apiKey = "2d51650e8cf7b5a2d13b814001a0dd30"
        val genreID = intent.getStringExtra("genre_id")
        val apiService = RetrofitInstance.getInstance().create(ListGenreMovieInterface::class.java)

        apiService.getListGenreMovie(apiKey , genreID.toString()).enqueue(object : Callback<MovieResponse> {

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movieModels)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }
}