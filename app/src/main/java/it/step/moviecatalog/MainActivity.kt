package it.step.moviecatalog

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar  // Importa la Toolbar corretta
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import it.step.moviecatalog.fragment.CategoryFragment
import it.step.moviecatalog.viewmodel.MovieViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var topAppBar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController

    private val movieViewModel: MovieViewModel by viewModels()


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
                    topAppBar.setNavigationOnClickListener {
                        drawerLayout.openDrawer(navigationView)
                    }
                }
            } else {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_hamburger)
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

    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


}








