package com.example.mymv.services

import com.example.mymv.models.GenreResponse
import retrofit2.Call
import retrofit2.http.GET

interface ListGenreInterface {
    @GET("/3/genre/movie/list?api_key=2d51650e8cf7b5a2d13b814001a0dd30")
    fun getGenreList(): Call<GenreResponse>
}