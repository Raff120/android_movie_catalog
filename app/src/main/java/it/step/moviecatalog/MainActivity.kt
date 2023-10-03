package it.step.moviecatalog

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import it.step.moviecatalog.viewmodel.MovieViewModel
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var topAppBar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var optionNavigationView: NavigationView
    private lateinit var navController: NavController

    private val movieViewModel: MovieViewModel by viewModels()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        topAppBar = findViewById(R.id.topAppBar)
        setSupportActionBar(topAppBar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.ma_nav_graph_host) as NavHostFragment
        navController = navHostFragment.navController


        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)



        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.categoryFragment || destination.id == R.id.detailsFragment || destination.id == R.id.searchFragment) {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_24)

                topAppBar.setNavigationOnClickListener {
                    navController.navigateUp()

                }
            } else {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_hamburger)
                topAppBar.setNavigationOnClickListener {
                    drawerLayout.openDrawer(navigationView)
                }
            }
        }


        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.categorie -> {
                    navController.navigate(R.id.categoryFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                R.id.cerca -> {
                    navController.navigate(R.id.searchFragment)
                    drawerLayout.closeDrawers()
                    true
                }

                else -> false
            }
        }

        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.italian -> {
//                    Toast.makeText(this, "Italiano", Toast.LENGTH_LONG).show()
                    // Imposta la lingua italiana
                    setAppLanguage(this, "it")
                    // Ricarica l'attività per applicare la nuova lingua
                    recreate()
                    true
                }

                R.id.english -> {
//                    Toast.makeText(this, "Inglese", Toast.LENGTH_LONG).show()
                    // Imposta la lingua inglese
                    setAppLanguage(this, "en")
                    // Ricarica l'attività per applicare la nuova lingua
                    recreate()
                    true
                }

                else -> false
            }
        }


//        optionNavigationView.setNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.italian -> {
//                    Toast.makeText(this, "Italiano", Toast.LENGTH_LONG).show()
//                    true
//                }
//
//                R.id.english -> {
//                    Toast.makeText(this, "Inglese", Toast.LENGTH_LONG).show()
//                    true
//                }
//
//                else -> false
//            }
//        }

    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun setAppLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Infla il menu; ciò aggiunge elementi alla barra delle azioni se presente.
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

}








