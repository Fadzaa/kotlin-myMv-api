package com.example.mymv.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.mymv.*
import com.example.mymv.adapter.homeAdapter.ListPosterAdapter
import com.example.mymv.adapter.homeAdapter.ListGenreAdapter
import com.example.mymv.adapter.seriesAdapter.ListPosterSeriesAdapter
import com.example.mymv.adapter.seriesAdapter.OnAirPosterAdapter
import com.example.mymv.detail.MovieDetail
import com.example.mymv.detail.SeriesDetail
import com.example.mymv.models.*
import com.example.mymv.services.*
import com.example.mymv.services.seriesInterface.TopRatedTvInterface
import com.example.mymv.services.seriesInterface.TrendingTVInterface
import kotlinx.android.synthetic.main.fragment_home_fragments.*
import kotlinx.android.synthetic.main.fragment_series.*
import kotlinx.android.synthetic.main.fragment_series.rvListGenre

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SeriesFragment : Fragment() {

    private lateinit var listPosterSeriesAdapter: ListPosterSeriesAdapter
    private lateinit var onAirPosterAdapter: OnAirPosterAdapter


    private lateinit var searchBar: Button
    private lateinit var listGenreAdapter: ListGenreAdapter

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_series, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBar = view.findViewById(R.id.search_bar_series)
//        setupChip()

        searchBar.setOnClickListener{
            startActivity(Intent(requireContext(), SearchBarSeries::class.java))
        }

        val snapHelper1 = PagerSnapHelper()
        val snapHelper2 = PagerSnapHelper()
        val snapHelper3 = PagerSnapHelper()
        val snapHelper4 = PagerSnapHelper()

        //List of Genre such as Action,Adventure and etc On CLIck
        listGenreAdapter = ListGenreAdapter(mutableListOf(), object: ListGenreAdapter.OnAdapterListener{
            override fun onClick(genreModel: GenreModel) {

                Log.d("ErrorCheck", "This is ${genreModel.name}")
                startActivity(Intent(requireContext() , ListGenreSeries::class.java)
                    .putExtra("genre_id", genreModel.id)
                    .putExtra("genre_name", genreModel.name)
                )


            }

        })

        //fetch to recyclerview
        snapHelper1.attachToRecyclerView(rvListGenre)
        rvListGenre.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvListGenre.setHasFixedSize(true)
        getListGenre{ genreModels : List<GenreModel> ->
            rvListGenre.adapter = ListGenreAdapter(genreModels, listGenreAdapter.listener)
        }


        //Main Poster for Series Fragment onCick

        onAirPosterAdapter = OnAirPosterAdapter(mutableListOf(), object: OnAirPosterAdapter.OnAdapterListener{
            override fun onClick(tvModel: TVModel) {
                startActivity(
                    Intent(requireContext() , SeriesDetail::class.java)
                    .putExtra("tv_id", tvModel.id)
                )
            }

        })

        //fecth to recyclerview
        snapHelper2.attachToRecyclerView(rv_on_air_series)
        rv_on_air_series.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        rv_on_air_series.setHasFixedSize(true)
        getOnAirTvData { tvModels : List<TVModel> ->
            rv_on_air_series.adapter = OnAirPosterAdapter(tvModels, onAirPosterAdapter.listener)
        }


        listPosterSeriesAdapter = ListPosterSeriesAdapter(mutableListOf(), object: ListPosterSeriesAdapter.OnAdapterListener{
            override fun onClick(tvModel: TVModel) {
                startActivity(
                    Intent(requireContext() , SeriesDetail::class.java)
                        .putExtra("tv_id", tvModel.id)
                )
            }

        })

        snapHelper3.attachToRecyclerView(rv_trending_series)
        rv_trending_series.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        rv_trending_series.setHasFixedSize(true)
        getTrendingTvData { tvModels : List<TVModel> ->
            rv_trending_series.adapter = ListPosterSeriesAdapter(tvModels,listPosterSeriesAdapter.listener)
        }

        snapHelper4.attachToRecyclerView(rv_top_rated_series)
        rv_top_rated_series.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        rv_top_rated_series.setHasFixedSize(true)
        getTopRatedTvData { tvModels : List<TVModel> ->
            rv_top_rated_series.adapter = ListPosterSeriesAdapter(tvModels,listPosterSeriesAdapter.listener)
        }



    }

    //Get list of genre for TV
    private fun getListGenre(callback: (List<GenreModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(ListGenreTvInterface::class.java)
        apiService.getGenreList().enqueue(object : Callback<GenreResponse> {
            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                return callback(response.body()!!.genreModels)
            }

            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {

            }

        })
    }

    //Get API ON AIR TV
    private fun getOnAirTvData(callback: (List<TVModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(TopRatedTvInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<TVResponse> {
            override fun onResponse(call: Call<TVResponse>, response: Response<TVResponse>) {
                return callback(response.body()!!.tvModels)
            }

            override fun onFailure(call: Call<TVResponse>, t: Throwable) {

            }

        })
    }

    //Get API Trending TV
    private fun getTrendingTvData(callback: (List<TVModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(TrendingTVInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<TVResponse> {
            override fun onResponse(call: Call<TVResponse>, response: Response<TVResponse>) {
                return callback(response.body()!!.tvModels)
            }

            override fun onFailure(call: Call<TVResponse>, t: Throwable) {

            }

        })
    }

    //Get API TOP RATED TV
    private fun getTopRatedTvData(callback: (List<TVModel>) -> Unit) {
        val apiService = RetrofitInstance.getInstance().create(TopRatedTvInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<TVResponse> {
            override fun onResponse(call: Call<TVResponse>, response: Response<TVResponse>) {
                return callback(response.body()!!.tvModels)
            }

            override fun onFailure(call: Call<TVResponse>, t: Throwable) {

            }

        })
    }
}