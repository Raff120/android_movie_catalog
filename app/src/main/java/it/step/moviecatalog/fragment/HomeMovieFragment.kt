package it.step.moviecatalog.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import it.step.moviecatalog.R
import it.step.moviecatalog.adapter.MovieAdapter
import it.step.moviecatalog.databinding.FragmentHomeMovieBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel
import java.net.ConnectException

class HomeMovieFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private var moviesList : List<Movie> = emptyList()
    private lateinit var bindingHomeMovies: FragmentHomeMovieBinding
    private lateinit var view : View
    private var listLoaded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if(isNetworkConnected(requireContext())) {

            movieViewModel.initMovieList()

            // Inflate the layout for this fragment
            bindingHomeMovies = FragmentHomeMovieBinding.inflate(layoutInflater)
            view = bindingHomeMovies.root
        }else{
            view = inflater.inflate(R.layout.no_connection_layout, container, false)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(isNetworkConnected(requireContext())){
            val recyclerView: RecyclerView = view.findViewById(R.id.hmf_all_movies_recycler)

            // Create the observer which updates the UI.
            val movieListObserver = Observer<List<Movie>?> { newMovieList ->
                // Update the UI
                if (newMovieList != null) {
                    moviesList = newMovieList
                    movieAdapter = MovieAdapter(moviesList){ movie ->
                        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(movie.imdbID
                        )
                        findNavController().navigate(action)


                    }
                    val layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = movieAdapter

                    if(newMovieList.isEmpty()) bindingHomeMovies.hmfMessage.text = "Lista vuota"
                    else bindingHomeMovies.hmfMessage.text = ""
                }
            }

            // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
            movieViewModel.moviesList.observe(viewLifecycleOwner, movieListObserver)

            val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL )
            recyclerView.addItemDecoration(divider)
        }


    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


}

