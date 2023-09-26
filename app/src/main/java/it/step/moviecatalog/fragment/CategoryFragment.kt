package it.step.moviecatalog.fragment

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.divider.MaterialDividerItemDecoration
import it.step.moviecatalog.MainActivity
import it.step.moviecatalog.R
import it.step.moviecatalog.adapter.MovieAdapter
import it.step.moviecatalog.databinding.FragmentCategoryBinding
import it.step.moviecatalog.databinding.FragmentDetailsBinding
import it.step.moviecatalog.databinding.FragmentHomeMovieBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter
    private var moviesList: List<Movie> = emptyList()
    private lateinit var bindingCategory: FragmentCategoryBinding
    private lateinit var view: View
    private var selectedGenre: String? = null
    private lateinit var mainActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = requireActivity() as MainActivity
        if ((mainActivity as MainActivity).isNetworkConnected(requireContext())) {

            movieViewModel.getAllMoviesByCategories()

            // Inflate the layout for this fragment
            bindingCategory = FragmentCategoryBinding.inflate(layoutInflater)
            view = bindingCategory.root
        } else {
            view = inflater.inflate(R.layout.no_connection_layout, container, false)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if ((mainActivity as MainActivity).isNetworkConnected(requireContext())) {

            movieViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    bindingCategory.cfProgressBar.visibility =
                        View.VISIBLE // Mostra la ProgressBar
                } else {
                    bindingCategory.cfProgressBar.visibility =
                        View.GONE // Nasconde la ProgressBar
                }
            }



            val validChipId = setOf(
                R.id.chip4, R.id.chip3, R.id.chip, R.id.chip2, R.id.chip6,
                R.id.chip8, R.id.chip9, R.id.chip10, R.id.chip11, R.id.chip12,
                R.id.chip15, R.id.chip17
            )

            var selectedChipId: Int = R.id.chip4
            //Gestione Selezione Chip non è possibile deselezionare una chip selezionata
            bindingCategory.chipGroup.setOnCheckedChangeListener { group, checkedId ->
                if (!validChipId.contains(checkedId)) {
                    group.check(selectedChipId)
                } else {
                    when (checkedId) {
                        R.id.chip4 -> movieViewModel.getAllMoviesByCategories()
                        R.id.chip3 -> movieViewModel.findByGenre("action")
                        R.id.chip -> movieViewModel.findByGenre("Adventure")
                        R.id.chip2 -> movieViewModel.findByGenre("Animation")
                        R.id.chip6 -> movieViewModel.findByGenre("Crime")
                        R.id.chip8 -> movieViewModel.findByGenre("Comedy")
                        R.id.chip9 -> movieViewModel.findByGenre("Drama")
                        R.id.chip10 -> movieViewModel.findByGenre("Family")
                        R.id.chip11 -> movieViewModel.findByGenre("Fantasy")
                        R.id.chip12 -> movieViewModel.findByGenre("History")
                        R.id.chip15 -> movieViewModel.findByGenre("Short")
                        R.id.chip17 -> movieViewModel.findByGenre("Thriller")
                    }
                    selectedChipId = checkedId
                }
            }
            val recyclerView: RecyclerView = bindingCategory.cfAllmovieRecycle

            movieViewModel.moviesList.removeObservers(viewLifecycleOwner)

            val movieListObserver = Observer<List<Movie>?> { newMovieList ->
                if (newMovieList != null) {
                    moviesList = newMovieList
                    movieAdapter = MovieAdapter(moviesList) { movie ->
                        val action =
                            CategoryFragmentDirections.actionCategoryFragmentToDetailsFragment(
                                movie.imdbID
                            )
                        findNavController().navigate(action)
                    }
                    val layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = movieAdapter

                    if (newMovieList.isEmpty()) {
                        bindingCategory.cfMessage.text = getString(R.string.empty_list)
                    } else {
                        bindingCategory.cfMessage.text = getString(R.string.empty_string)
                    }
                }
            }

            // Osserva la LiveData per il genere selezionato
//            movieViewModel.findByGenre(genre)
            movieViewModel.genreMovies.observe(viewLifecycleOwner, movieListObserver)

        }


    }

}