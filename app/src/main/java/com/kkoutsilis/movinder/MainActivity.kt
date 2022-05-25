package com.kkoutsilis.movinder

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.kkoutsilis.movinder.models.Movie
import com.kkoutsilis.movinder.services.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var movieData: TextView? = null
    private var movieId: TextInputEditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movieData = findViewById(R.id.textView)
        movieId = findViewById(R.id.textInput)

        findViewById<View>(R.id.button).setOnClickListener { (getMovieData(movieId?.text.toString())) }

    }

    internal fun getMovieData(movieId: String) {
        val retrofit: Retrofit? = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit?.create(MovieService::class.java)
        val token =
            "<token>"

        service?.getMovie(movieId, "Bearer $token")
            ?.enqueue(object : Callback<Movie> {
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    Log.i("response",response.toString())
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