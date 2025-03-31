package br.com.bitsolutions.mercadolivre.domain.favorite.usecase

import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.favorite.FavoriteRepository
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class GetItemsListUseCase(private val repository: FavoriteRepository) {
    fun execute(): Flow<List<SearchResultItem>> {
        return getItemsList().onStart { State.loading<List<SearchResultItem>>() }
    }

    private fun getItemsList(): Flow<List<SearchResultItem>> = repository.getItemsList()
}
