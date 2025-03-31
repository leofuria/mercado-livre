package br.com.bitsolutions.mercadolivre.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import br.com.bitsolutions.mercadolivre.domain.home.usecase.SearchUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(
    private val searchUseCase: SearchUseCase,
) : ViewModel() {

    private val limit = 50
    private var cList = emptyList<SearchResultItem>()
    private val _hasNextPage = MutableStateFlow(false)
    val hasNextPage = _hasNextPage.asStateFlow()

    private val _resultItems = MutableStateFlow<State<SearchResult?>>(State.idle())
    val resultItems = _resultItems.asStateFlow()

    fun getSearchResult(siteId: String, query: String, offset: Int, fileString: String? = null) {
        viewModelScope.launch {
            val mOffset = if (hasNextPage.value) offset + limit else offset
            if (hasNextPage.value == false) cList = emptyList()

            searchUseCase.execute(siteId, query, mOffset, limit, fileString)
                .map { event ->
                    (event as? State.Data)?.data?.let {
                        _hasNextPage.emit(it.total > it.offset)

                        val nList = it.items
                        cList = cList + nList

                        State.data(
                            SearchResult(
                                cList,
                                it.query,
                                it.total,
                                it.primaryResults,
                                it.offset,
                                it.limit,
                            ),
                        )
                    } ?: event
                }
                .collect {
                    _resultItems.emit(it)
                }
        }
    }
}
