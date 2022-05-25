package com.kkoutsilis.movinder.services

import com.kkoutsilis.movinder.models.Movie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MovieService {
    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movie_id: String, @Header("Authorization") authHeader: String): Call<Movie>
}