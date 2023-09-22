package it.step.moviecatalog.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import it.step.moviecatalog.R
import it.step.moviecatalog.databinding.FragmentDetailsBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel
class DetailsFragment : Fragment() {

    private lateinit var bindingDetails: FragmentDetailsBinding
    private lateinit var view : View
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        movieViewModel.getByID(args?.getString("imdbID").toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(isNetworkConnected(requireContext())) {
            bindingDetails = FragmentDetailsBinding.inflate(layoutInflater)
            view = bindingDetails.root


            val movieObserver = Observer<Movie?> { newMovie ->
                // Update the UI
                if (newMovie != null) {
                    bindingDetails.imPosterDetails.load(newMovie.poster)
                    bindingDetails.titleDetails.text = newMovie.title
                    bindingDetails.plotDetails.text = newMovie.plot
                    bindingDetails.listActorsDetails.text = newMovie.actors
                    bindingDetails.directorDetails.text = newMovie.director
                }
            }

            // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
            movieViewModel.movie.observe(viewLifecycleOwner, movieObserver)
        }
        else{
            view = inflater.inflate(R.layout.no_connection_layout, container, false)
        }

        return view
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}