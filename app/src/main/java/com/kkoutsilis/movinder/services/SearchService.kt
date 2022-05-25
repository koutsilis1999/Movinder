package com.kkoutsilis.movinder.services

import com.kkoutsilis.movinder.models.MovieSearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchService {

    @GET("search/movie")
    fun searchMovie(
        @Query("query") query: String,
        @Header("Authorization") authHeader: String
    ): Call<MovieSearchResult>
}