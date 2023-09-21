package it.step.moviecatalog.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import it.step.moviecatalog.R
import it.step.moviecatalog.adapter.MovieAdapter
import it.step.moviecatalog.databinding.FragmentSearchBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel

class SearchFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private var searchList: List<Movie> = emptyList()
    private lateinit var bindingSearch: FragmentSearchBinding
    private lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (isNetworkConnected(requireContext())) {

            movieViewModel.initSearchList()

            // Inflate the layout for this fragment
            bindingSearch = FragmentSearchBinding.inflate(layoutInflater)
            view = bindingSearch.root
        } else {
            view = inflater.inflate(R.layout.no_connection_layout, container, false)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                bindingSearch.sfProgressBar.visibility = View.VISIBLE // Mostra la ProgressBar
            } else {
                bindingSearch.sfProgressBar.visibility = View.GONE // Nasconde la ProgressBar
            }
        }

        if (isNetworkConnected(requireContext())) {
            val recyclerView: RecyclerView = view.findViewById(R.id.sf_searched_recycler)

            // Create the observer which updates the UI.
            val searchListObserver = Observer<List<Movie>?> { newSearchList ->
                // Update the UI
                if (newSearchList != null) {
                    searchList = newSearchList
                    movieAdapter = MovieAdapter(searchList){ movie ->
                        //TODO
                    }
                    val layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = movieAdapter

                    if (newSearchList.isEmpty()) bindingSearch.sfMessage.text =
                        getString(R.string.empty_list)
                    else bindingSearch.sfMessage.text = getString(R.string.empty_string)
                }
            }

            // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
            movieViewModel.searchedMovies.observe(viewLifecycleOwner, searchListObserver)

            val divider =
                MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
            recyclerView.addItemDecoration(divider)
        }

        bindingSearch.sfSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                movieAdapter?.getFilter()?.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                movieAdapter?.getFilter()?.filter(newText);
                return true
            }

        })
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}