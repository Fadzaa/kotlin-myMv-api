package com.example.mymv.services

import com.example.mymv.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

interface TopRatedMovie {
    @GET("/3/movie/top_rated?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getMovieList(): Call<MovieResponse>
}