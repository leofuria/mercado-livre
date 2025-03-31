package br.com.bitsolutions.mercadolivre.domain.favorite

import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getItemsList(): Flow<List<SearchResultItem>>
    suspend fun insertItem(item: SearchResultItem)
    suspend fun deleteItem(item: SearchResultItem)
}
