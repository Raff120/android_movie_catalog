package it.step.moviecatalog.fragment

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import it.step.moviecatalog.MainActivity
import it.step.moviecatalog.R
import it.step.moviecatalog.adapter.MovieAdapter
import it.step.moviecatalog.databinding.FragmentHomeSeriesBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel

class HomeSeriesFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private var seriesList: List<Movie> = emptyList()
    private lateinit var bindingHomeSeries: FragmentHomeSeriesBinding
    private lateinit var view: View
    private lateinit var mainActivity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = requireActivity() as MainActivity

        movieViewModel.initSeriesList()

        // Inflate the layout for this fragment
        bindingHomeSeries = FragmentHomeSeriesBinding.inflate(layoutInflater)
        view = bindingHomeSeries.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!(mainActivity as MainActivity).isNetworkConnected(requireContext()))
            bindingHomeSeries.hsfMessage.text = getString(R.string.no_connection)
        else bindingHomeSeries.hsfMessage.text = getString(R.string.empty_string)

        bindingHomeSeries.hsfSwipeRefreshLayout.setOnRefreshListener {
            if ((mainActivity as MainActivity).isNetworkConnected(requireContext())) {
                bindingHomeSeries.hsfRecyclerLayout.visibility = View.VISIBLE
                bindingHomeSeries.hsfErrorLayout.visibility = View.GONE
                bindingHomeSeries.hsfMessage.text = ""
                movieViewModel.initSeriesList()
            } else {
                bindingHomeSeries.hsfErrorLayout.visibility = View.VISIBLE
                bindingHomeSeries.hsfRecyclerLayout.visibility = View.GONE
                bindingHomeSeries.hsfMessage.text = getString(R.string.no_connection)
            }
            bindingHomeSeries.hsfSwipeRefreshLayout.isRefreshing = false
        }

        movieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                bindingHomeSeries.hsfProgressBar.visibility =
                    View.VISIBLE // Mostra la ProgressBar
            } else {
                bindingHomeSeries.hsfProgressBar.visibility =
                    View.GONE // Nasconde la ProgressBar
            }
        }

        bindingHomeSeries.msfToggleButton?.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.msf_sortAZButton -> {
                        movieAdapter.sortMoviesAlphabeticallyAZ()
                    }

                    R.id.msf_sortZAButton -> {
                        movieAdapter.sortMoviesAlphabeticallyZA()
                    }
                }
            } else {
                if (group.checkedButtonId == View.NO_ID) {
                    movieViewModel.initSeriesList()
                }
            }
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.hsf_all_series_recycler)

        // Create the observer which updates the UI.
        val seriesListObserver = Observer<List<Movie>?> { newSeriesList ->
            // Update the UI
            if (newSeriesList != null) {
                seriesList = newSeriesList
                movieAdapter = MovieAdapter(seriesList) { movie ->
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                        movie.imdbID
                    )
                    findNavController().navigate(action)
                }
                val layoutManager = LinearLayoutManager(requireContext())
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = movieAdapter

//                if (newSeriesList.isEmpty()) bindingHomeSeries.hsfMessage.text =
//                    getString(R.string.empty_list)
//                else bindingHomeSeries.hsfMessage.text = getString(R.string.empty_string)
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        movieViewModel.seriesList.observe(viewLifecycleOwner, seriesListObserver)

    }

    override fun onResume() {
        super.onResume()
        if(!(mainActivity as MainActivity).isNetworkConnected(requireContext()))
            bindingHomeSeries.hsfMessage.text = getString(R.string.no_connection)
        else{
            bindingHomeSeries.hsfMessage.text = getString(R.string.empty_string)
            movieViewModel.initSeriesList()
        }



    }


}