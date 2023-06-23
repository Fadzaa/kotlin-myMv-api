package com.example.mymv.services

import com.example.mymv.models.MovieResponse
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