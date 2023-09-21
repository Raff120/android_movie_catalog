package it.step.moviecatalog.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bumptech.glide.Glide
import it.step.moviecatalog.R
import it.step.moviecatalog.adapter.MovieAdapter
import it.step.moviecatalog.databinding.FragmentDetailsBinding
import it.step.moviecatalog.databinding.FragmentHomeBinding
import it.step.moviecatalog.model.Movie
import it.step.moviecatalog.viewmodel.MovieViewModel
import java.net.HttpURLConnection
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        bindingDetails = FragmentDetailsBinding.inflate(layoutInflater)
        view = bindingDetails.root


        val movieObserver = Observer<Movie?> { newMovie ->
            // Update the UI
            if (newMovie != null) {
                bindingDetails.imageView2.load(newMovie.poster)
                bindingDetails.textView3.text=newMovie.title
                bindingDetails.textView4.text=newMovie.plot
                bindingDetails.textView6.text=newMovie.actors
                bindingDetails.textView8.text=newMovie.director
            }
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        movieViewModel.movie.observe(viewLifecycleOwner, movieObserver)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}