package com.example.mymv.services.detailsInterface

import com.example.mymv.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailsSeriesInterface {
    @GET("tv/{tvId}?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getSeriesDetails(@Path("tvId") movieId: Int): Call<TVModel>
}



interface RecommendationSeriesInterface {
    @GET("tv/{tvId}/recommendations?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getRecommendationDetails(@Path("tvId") movieId: Int): Call<TVResponse>
}