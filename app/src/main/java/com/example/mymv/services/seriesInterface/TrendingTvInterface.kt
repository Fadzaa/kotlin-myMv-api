package com.example.mymv.services.seriesInterface

import com.example.mymv.models.TVResponse
import retrofit2.Call
import retrofit2.http.GET

interface TrendingTVInterface {
    @GET("/3/trending/tv/week?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getMovieList(): Call<TVResponse>
}