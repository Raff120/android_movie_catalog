package it.step.moviecatalog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar  // Importa la Toolbar corretta
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView
import it.step.moviecatalog.viewmodel.MovieViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var topAppBar: Toolbar  // Usa la Toolbar corretta
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



        // Imposta l'OnClickListener dell'AppBar
        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(navigationView)
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Verifica la destinazione corrente e cambia l'icona dell'AppBar di conseguenza
            if (destination.id == R.id.categoryFragment || destination.id == R.id.detailsFragment || destination.id == R.id.searchFragment) {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_24)
                topAppBar.setNavigationOnClickListener {
                    navController.navigateUp()
                    topAppBar.setNavigationOnClickListener {
                        drawerLayout.openDrawer(navigationView)
                    }
                }
            } else  {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_hamburger)
            }
        }

    }

}
