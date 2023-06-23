package com.example.mymv.services

import com.example.mymv.models.MovieResponse
import com.example.mymv.models.TVResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiInterface {
    @GET("search/movie?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<MovieResponse>
}

interface SearchApiSeriesInterface {
    @GET("search/tv?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun searchSeries(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<TVResponse>
}