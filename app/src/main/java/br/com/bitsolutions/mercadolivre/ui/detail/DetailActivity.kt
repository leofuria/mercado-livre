package br.com.bitsolutions.mercadolivre.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bitsolutions.mercadolivre.R
import br.com.bitsolutions.mercadolivre.databinding.ActivityDetailBinding
import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchItemPriceInstallments
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import br.com.bitsolutions.mercadolivre.ui.utils.Condition
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val itemId by lazy { intent?.getStringExtra(ITEM_ID) }
    private val seller by lazy { intent?.getStringExtra(SELLER) }
    private val installments by lazy { intent?.getSerializableExtra(INSTALLMENTS) as? SearchItemPriceInstallments }
    val viewModel by viewModel<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            },
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.resultItem.collectLatest {
                        onLoading(it.isLoading())
                        when (it) {
                            is State.Data -> onSuccessFetchItem(it.data)
                            is State.Error -> onError(it)
                            else -> Unit
                        }
                    }
                }
            }
        }

        viewModel.getProductItem(itemId ?: "")
    }

    fun onLoading(loading: Boolean?) {
        binding.errorView.visibility = View.GONE
        if (loading == true) {
            binding.loadingView.visibility = View.VISIBLE
        } else {
            binding.loadingView.visibility = View.GONE
        }
    }

    private fun onSuccessFetchItem(data: SearchResultItem?) {
        data?.let { item ->
            Condition.fromBuildType(item.condition)?.resId?.let { resId ->
                binding.tvResultCondition.text = getString(resId)
            }
            binding.tvResultTitle.text = item.title
            binding.tvResultSelLer.text = seller
            binding.vpResultImages.adapter = CarouselAdapter(
                ArrayList(item.pictures ?: emptyList<String>()),
            )
            TabLayoutMediator(binding.intoTabLayout, binding.vpResultImages) { tab, position -> }.attach()

            binding.tvResultOriginalPrice.visibility = if (item.price.formatedOriginalAmount.isBlank()) View.GONE else View.VISIBLE
            binding.tvResultOriginalPrice.text = item.price.formatedOriginalAmount
            binding.tvResultOriginalPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvResultPrice.text = item.price.formatedAmount
            installments?.let {
                binding.tvResultInstallments.text = String.format("${it.quantity}x ${it.formatedAmount}")
            }
            binding.rvResultAttributes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            if (item.attributes.isNotEmpty()) {
                binding.tvResultAttributesLabel.visibility = View.VISIBLE
                binding.rvResultAttributes.visibility = View.VISIBLE
            }
            binding.rvResultAttributes.adapter = DetailAttributeAdapter(item.attributes)
        } ?: run {
            binding.errorView.visibility = View.VISIBLE
            binding.errorView.bringToFront()
            binding.errorView.showFeedbackStatus(
                feedbackTitle = R.string.paged_list_search_not_found_title,
                feedbackMessage = R.string.paged_list_search_not_found_description,
            )
        }
    }

    private fun onError(error: State.Error?) {
        if (error == null) return

        binding.errorView.visibility = View.VISIBLE
        binding.errorView.bringToFront()
        binding.errorView.showFeedbackStatus(
            feedbackTitle = R.string.paged_list_generic_error,
            feedbackMessage = R.string.paged_list_verify_connection_label,
            action = {
                viewModel.getProductItem(itemId ?: "")
            },
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val ITEM_ID = "item_id"
        const val SELLER = "seller"
        const val INSTALLMENTS = "installments"

        fun launch(context: Context, itemId: String, seller: String, installments: SearchItemPriceInstallments?) {
            context.startActivity(
                Intent(context, DetailActivity::class.java).apply {
                    putExtra(ITEM_ID, itemId)
                    putExtra(SELLER, seller)
                    putExtra(INSTALLMENTS, installments)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                },
            )
        }
    }
}
