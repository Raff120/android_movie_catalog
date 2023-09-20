package it.step.moviecatalog.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import it.step.moviecatalog.R
import it.step.moviecatalog.configuration.ApiManager
import it.step.moviecatalog.databinding.FragmentHomeBinding
import it.step.moviecatalog.viewmodel.MovieViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private val movieViewModel: MovieViewModel by viewModels()

    private lateinit var bindingHome: FragmentHomeBinding
    private lateinit var view : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingHome = FragmentHomeBinding.inflate(layoutInflater)
        view = bindingHome.root

        val buttonToCategory = view.findViewById<Button>(R.id.button2)

        buttonToCategory.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_homeFragment_to_categoryFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}