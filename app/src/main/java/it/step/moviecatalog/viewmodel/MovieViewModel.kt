package it.step.moviecatalog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.step.moviecatalog.configuration.ApiManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieViewModel : ViewModel() {

    private var apiService = ApiManager.movieService

    fun getByID(id: String){
        viewModelScope.launch {
            val response = apiService.getMovieByID(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.i("TEST", "${response.body()?.title}")
                } else {
                    Log.i("TEST", "Errore ${response.body()?.title}")
                }
            }
        }

    }

    

}