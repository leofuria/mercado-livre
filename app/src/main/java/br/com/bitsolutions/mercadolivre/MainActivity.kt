package br.com.bitsolutions.mercadolivre

import android.content.Context
import android.content.Intent
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
    var mMenu: Menu? = null

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
                R.id.navigation_settings,
            ),
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            mMenu?.findItem(R.id.search_bar)?.isVisible = destination.id == R.id.navigation_home
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        mMenu = menu
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val searchView = menu.findItem(R.id.search_bar).actionView as? SearchView
        searchView?.queryHint = getString(R.string.search_hint)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length < 3) {
                    Toast.makeText(this@MainActivity, R.string.search_toast_message, Toast.LENGTH_LONG).show()
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

    companion object {
        const val QUERY = "query"

        fun launch(context: Context, query: String) {
            context.startActivity(
                Intent(context, MainActivity::class.java).apply {
                    putExtra(QUERY, query)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                },
            )
        }
    }
}
