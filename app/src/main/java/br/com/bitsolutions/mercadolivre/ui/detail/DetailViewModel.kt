package br.com.bitsolutions.mercadolivre.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.detail.usecase.DetailUseCase
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val detailUseCase: DetailUseCase,
) : ViewModel() {

    private val _resultItem = MutableStateFlow<State<SearchResultItem?>>(State.idle())
    val resultItem = _resultItem.asStateFlow()

    fun getProductItem(productId: String) {
        viewModelScope.launch {
            detailUseCase.execute(productId)
                .collect {
                    _resultItem.emit(it)
                }
        }
    }
}
