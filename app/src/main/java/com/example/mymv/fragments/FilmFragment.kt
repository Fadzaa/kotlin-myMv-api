package com.example.mymv.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymv.MovieDetail
import com.example.mymv.R
import com.example.mymv.adapter.ListPosterAdapter
import com.example.mymv.models.Movie
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.*
import kotlinx.android.synthetic.main.fragment_film.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilmFragment : Fragment() {

    private lateinit var listPosterAdapter: ListPosterAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_film, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listPosterAdapter = ListPosterAdapter(mutableListOf(), object: ListPosterAdapter.OnAdapterListener{
            override fun onClick(movie: Movie) {
                startActivity(
                    Intent(requireContext() , MovieDetail::class.java)
                    .putExtra("title_movie", movie.title)
                    .putExtra("poster_movie", movie.poster)
                    .putExtra("movie_release", movie.release)
                    .putExtra("movie_overview", movie.overview)
                    .putExtra("backdrop_path", movie.backdropPath)
                )
            }

        })

        popular_tv_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        popular_tv_list.setHasFixedSize(true)
        getPopularTvData { movies : List<Movie> ->
            popular_tv_list.adapter = ListPosterAdapter(movies,listPosterAdapter.listener)
        }

        on_air_tv_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        on_air_tv_list.setHasFixedSize(true)
        getTopRatedTvData { movies : List<Movie> ->
            on_air_tv_list.adapter = ListPosterAdapter(movies,listPosterAdapter.listener)
        }

        top_rated_tv_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        top_rated_tv_list.setHasFixedSize(true)
        getOnAirTvData { movies : List<Movie> ->
            top_rated_tv_list.adapter = ListPosterAdapter(movies,listPosterAdapter.listener)
        }

//        latest_tv_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
//        latest_tv_list.setHasFixedSize(true)
//        getLatestTvData { movies : List<Movie> ->
//            latest_tv_list.adapter = ListPosterAdapter(movies,listPosterAdapter.listener)
//        }
    }

    private fun getPopularTvData(callback: (List<Movie>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(PopularTvInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }

    private fun getOnAirTvData(callback: (List<Movie>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(TopRatedTvInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }

    private fun getTopRatedTvData(callback: (List<Movie>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(OnAirTvInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }

    private fun getLatestTvData(callback: (List<Movie>) -> Unit) {
        val apiService = MovieApiService.getInstance().create(LatestTVInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }



}