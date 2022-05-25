package com.kkoutsilis.movinder

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.kkoutsilis.movinder.models.Movie
import com.kkoutsilis.movinder.models.MovieSearchResult
import com.kkoutsilis.movinder.services.MovieService
import com.kkoutsilis.movinder.services.SearchService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var movieData: TextView? = null
    private var searchQuery: TextInputEditText? = null
    private var token:String = "TheMovieDB token here"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movieData = findViewById(R.id.textView)
        searchQuery = findViewById(R.id.textInput)
        findViewById<View>(R.id.button).setOnClickListener { (searchMovies(searchQuery?.text.toString())) }
    }

    internal fun searchMovies(searchQuery: String) {
        Log.i("fun","searchMovies was called")
        val retrofit: Retrofit? = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit?.create(SearchService::class.java)

        service?.searchMovie(query = searchQuery, authHeader = "Bearer $token")
            ?.enqueue(object : Callback<MovieSearchResult> {
                override fun onResponse(
                    call: Call<MovieSearchResult>,
                    response: Response<MovieSearchResult>
                ) {
                    Log.i("response", response.toString())
                    if (response.code() == 200) {
                        val searchResponse = response.body()
                        val movieTitles = ArrayList<String>()

                        if (searchResponse != null) {
                            for (movie: Movie in searchResponse.results) {
                                movieTitles.add(movie.title)
                            }


                            movieData?.text = movieTitles.toString()
                        }
                    }
                }

                override fun onFailure(call: Call<MovieSearchResult>, t: Throwable) {
                    movieData!!.text = t.message
                    Log.i("error", t.message.toString())
                }
            })
    }

    internal fun getMovieData(movieId: String) {
        val retrofit: Retrofit? = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit?.create(MovieService::class.java)

        service?.getMovie(movieId, "Bearer $token")
            ?.enqueue(object : Callback<Movie> {
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    Log.i("response", response.toString())
                    if (response.code() == 200) {
                        val movieResponse = response.body()

                        val stringBuilder = "adult: " +
                                (movieResponse?.adult ?: "haha") +
                                "\n" +
                                "id: " +
                                movieResponse?.id.toString() +
                                "\n" +
                                "Title: " +
                                (movieResponse?.title ?: "haha2")


                        movieData?.text = stringBuilder
                    }
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    movieData!!.text = t.message
                    Log.i("error", t.message.toString())
                }
            })
    }
}