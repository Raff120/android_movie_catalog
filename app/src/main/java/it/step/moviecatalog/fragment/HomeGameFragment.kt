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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import it.step.moviecatalog.R
import it.step.moviecatalog.adapter.MovieAdapter
import it.step.moviecatalog.databinding.FragmentHomeGameBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel

class HomeGameFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private var gamesList: List<Movie> = emptyList()
    private lateinit var bindingHomeGame: FragmentHomeGameBinding
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (isNetworkConnected(requireContext())) {

            movieViewModel.initGamesList()

            // Inflate the layout for this fragment
            bindingHomeGame = FragmentHomeGameBinding.inflate(layoutInflater)
            view = bindingHomeGame.root
        } else {
            view = inflater.inflate(R.layout.no_connection_layout, container, false)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                bindingHomeGame.hgfProgressBar.visibility = View.VISIBLE // Mostra la ProgressBar
            } else {
                bindingHomeGame.hgfProgressBar.visibility = View.GONE // Nasconde la ProgressBar
            }
        }

        if (isNetworkConnected(requireContext())) {
            val recyclerView: RecyclerView = view.findViewById(R.id.hgf_all_games_recycler)

            // Create the observer which updates the UI.
            val gameListObserver = Observer<List<Movie>?> { newGameList ->
                // Update the UI
                if (newGameList != null) {
                    gamesList = newGameList
                    movieAdapter = MovieAdapter(gamesList) { game ->
                        //TODO on click
                    }
                    val layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = movieAdapter

                    if (newGameList.isEmpty()) bindingHomeGame.hgfMessage.text =
                        getString(R.string.empty_list)
                    else bindingHomeGame.hgfMessage.text = getString(R.string.empty_string)
                }
            }

            // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
            movieViewModel.gamesList.observe(viewLifecycleOwner, gameListObserver)

            val divider =
                MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            recyclerView.addItemDecoration(divider)
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}