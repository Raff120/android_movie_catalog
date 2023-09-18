package it.step.moviecatalog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.step.moviecatalog.configuration.ApiManager
import it.step.moviecatalog.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieViewModel : ViewModel() {


    private var apiService = ApiManager.movieService
    val movie = MutableLiveData<Movie?>(null)
    val allMovies = MutableLiveData<List<Movie>?>(null)
    val searchedMovies = MutableLiveData<List<Movie>?>(null)
    val genreMovies = MutableLiveData<List<Movie>?>(null)
    val typeMovies = MutableLiveData<List<Movie>?>(null)
    val categories = MutableLiveData<List<String>>(listOf(
                                                    "Action","Adventure","Drama","Animation",
                                                    "Sci-Fi", "Fantasy", "Comedy", "Short",
                                                    "Family", "Crime", "Documentary", "Mystery",
                                                    "War", "Romance", "History", "Thriller", "Horror"))

    fun getAllMovies() {
        viewModelScope.launch {
            val response = apiService.getAllMovies()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        allMovies.postValue(response.body())
                    } else {
                        //TODO
                    }
                } else {
                    //TODO
                }
            }
        }
    }

    fun getByID(id: String) {
        viewModelScope.launch {
            val response = apiService.getMovieByID(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        movie.postValue(response.body())
                    } else {
                        //TODO
                    }
                } else {
                    //TODO
                }
            }
        }
    }

    fun findByTitle(title: String) {
        viewModelScope.launch {
            val response = apiService.findMovieByTitle(title)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        searchedMovies.postValue(response.body())
                    } else {
                        //TODO
                    }
                } else {
                    //TODO
                }
            }
        }
    }

    fun findByGenre(genre: String) {
        viewModelScope.launch {
            val response = apiService.findMovieByGenre(genre)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        genreMovies.postValue(response.body())
                    } else {
                        //TODO
                    }
                } else {
                    //TODO
                }
            }
        }
    }

    fun findByType(type: String) {
        viewModelScope.launch {
            val response = apiService.findMovieByType(type)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        typeMovies.postValue(response.body())
                    } else {
                        //TODO
                    }
                } else {
                    //TODO
                }
            }
        }
    }


}