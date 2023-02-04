package com.example.mymv.services

import com.example.mymv.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

interface OnAirTvInterface {
    @GET("/3/tv/on_the_air?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getMovieList(): Call<MovieResponse>
}