package com.example.mymv.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymv.MovieDetail
import com.example.mymv.R
import com.example.mymv.adapter.HomePosterAdapter
import com.example.mymv.adapter.ListPosterAdapter
import com.example.mymv.models.MovieModel
import com.example.mymv.models.MovieResponse
import com.example.mymv.services.LatestMovieInterface
import com.example.mymv.services.MovieApiInterface
import com.example.mymv.services.RetrofitInstance
import com.example.mymv.services.TopRatedMovie
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_home_fragments.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragments : Fragment() {

    private lateinit var homePosterAdapter: HomePosterAdapter
    private lateinit var listPosterAdapter: ListPosterAdapter
    private lateinit var chipGroup: ChipGroup


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
    : View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home_fragments, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipGroup = view.findViewById(R.id.chipGroup)
//        setupChip()



        homePosterAdapter = HomePosterAdapter(mutableListOf(), object: HomePosterAdapter.OnAdapterListener{
            override fun onClick(movieModel: MovieModel) {
                startActivity(Intent(requireContext() , MovieDetail::class.java)
                    .putExtra("title_movie", movieModel.title)
                    .putExtra("poster_movie", movieModel.poster)
                    .putExtra("movie_release", movieModel.release)
                    .putExtra("backdrop_path", movieModel.backdropPath)
                    .putExtra("movie_overview", movieModel.overview)
                )
            }

        })

        listPosterAdapter = ListPosterAdapter(mutableListOf(), object: ListPosterAdapter.OnAdapterListener{
            override fun onClick(movieModel: MovieModel) {
                startActivity(Intent(requireContext() , MovieDetail::class.java)
                    .putExtra("title_movie", movieModel.title)
                    .putExtra("poster_movie", movieModel.poster)
                    .putExtra("movie_release", movieModel.release)
                    .putExtra("backdrop_path", movieModel.backdropPath)
                    .putExtra("movie_overview", movieModel.overview)
                )
            }

        })

        rv_in_theatre.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_in_theatre.setHasFixedSize(true)
        getMovieData { movieModels : List<MovieModel> ->
            rv_in_theatre.adapter = HomePosterAdapter(movieModels,homePosterAdapter.listener)
        }

        top_rated_movie_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        top_rated_movie_list.setHasFixedSize(true)
        getTopRatedMovieData { movieModels : List<MovieModel> ->
            top_rated_movie_list.adapter = ListPosterAdapter(movieModels, listPosterAdapter.listener)
        }

        on_air_movie_list.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        on_air_movie_list.setHasFixedSize(true)
        getOnAirMovieData { movieModels : List<MovieModel> ->
            on_air_movie_list.adapter = ListPosterAdapter(movieModels, listPosterAdapter.listener)
        }
    }
//
//    private fun setupChip() {
//        val nameList =
//            arrayListOf("Action", "Comedy", "Drama", "Thriller")
//        for (name in nameList) {
//            val chip = createChip(name)
//            chipGroup.addView(chip)
//        }
//    }
//
//    private fun createChip(label: String): Chip {
//        val chip = Chip(requireContext())
//        chip.text = label
//        return chip
//    }


    private fun getMovieData(callback: (List<MovieModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movieModels)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }

    private fun getTopRatedMovieData(callback: (List<MovieModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(TopRatedMovie::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movieModels)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }

    private fun getOnAirMovieData(callback: (List<MovieModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(LatestMovieInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movieModels)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

            }

        })
    }






}