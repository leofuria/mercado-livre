package br.com.bitsolutions.mercadolivre

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.bitsolutions.mercadolivre.databinding.ActivityMainBinding
import br.com.bitsolutions.mercadolivre.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
            ),
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val searchView = menu.findItem(R.id.search_bar).actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length < 3) {
                    Toast.makeText(this@MainActivity, "enter at least 3 characters!", Toast.LENGTH_LONG).show()
                } else if (navController.currentDestination?.id == R.id.navigation_home) {
                    val navHostFragment = supportFragmentManager.fragments.first().childFragmentManager.fragments
                    (navHostFragment[0] as? HomeFragment)?.searchResultItems(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        if (navController.currentDestination?.id == R.id.navigation_home) {
            val navHostFragment = supportFragmentManager.fragments.first().childFragmentManager.fragments
            val queryText = (navHostFragment[0] as? HomeFragment)?.queryText ?: ""

            if (queryText.isNotBlank()) {
                searchView?.setOnQueryTextFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        searchView.setQuery(queryText, false)
                    }
                }
            }
        }

        return true
    }
}
