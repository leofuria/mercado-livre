package br.com.bitsolutions.mercadolivre.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.favorite.usecase.DeleteItemUseCase
import br.com.bitsolutions.mercadolivre.domain.favorite.usecase.GetItemsListUseCase
import br.com.bitsolutions.mercadolivre.domain.favorite.usecase.InsertItemUseCase
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val deleteItemUseCase: DeleteItemUseCase,
    private val insertItemUseCase: InsertItemUseCase,
    private val getItemsListUseCase: GetItemsListUseCase,
) : ViewModel() {

    private val _resultItems = MutableStateFlow<State<List<SearchResultItem>>>(State.idle())
    val resultItems = _resultItems.asStateFlow()

    fun getItemsList() {
        viewModelScope.launch {
            getItemsListUseCase.execute()
                .collect {
                    _resultItems.emit(State.data(it))
                }
        }
    }

    fun insertItem(searchResultItem: SearchResultItem) {
        viewModelScope.launch {
            insertItemUseCase.execute(searchResultItem)
        }
    }

    fun deleteItem(searchResultItem: SearchResultItem) {
        viewModelScope.launch {
            deleteItemUseCase.execute(searchResultItem)
        }
    }
}
