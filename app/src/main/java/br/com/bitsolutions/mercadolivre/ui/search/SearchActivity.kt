package br.com.bitsolutions.mercadolivre.ui.search

import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.bitsolutions.mercadolivre.MainActivity
import br.com.bitsolutions.mercadolivre.R
import br.com.bitsolutions.mercadolivre.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.length < 3) {
                    Toast.makeText(this@SearchActivity, R.string.search_toast_message, Toast.LENGTH_LONG).show()
                } else {
                    MainActivity.Companion.launch(this@SearchActivity, query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
}
