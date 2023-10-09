package it.step.moviecatalog.configuration

import it.step.moviecatalog.service.MovieService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    companion object {
//        val BASE_URL = "http://192.168.3.31:8080/movie-catalog/"
        val BASE_URL = "http://192.168.1.33:8080/movie-catalog/"
        var retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        val movieService = retrofit.create(MovieService::class.java)
    }

}