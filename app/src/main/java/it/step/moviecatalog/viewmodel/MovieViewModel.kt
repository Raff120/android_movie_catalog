package it.step.moviecatalog.viewmodel

import androidx.lifecycle.ViewModel
import it.step.moviecatalog.configuration.ApiManager

class MovieViewModel : ViewModel() {

    private var apiService = ApiManager.movieService

    

}