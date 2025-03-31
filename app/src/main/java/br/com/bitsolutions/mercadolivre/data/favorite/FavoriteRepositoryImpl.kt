package br.com.bitsolutions.mercadolivre.data.favorite

import br.com.bitsolutions.mercadolivre.data.favorite.local.FavoriteLocalData
import br.com.bitsolutions.mercadolivre.domain.favorite.FavoriteRepository
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class FavoriteRepositoryImpl(
    private val localData: FavoriteLocalData,
) : FavoriteRepository {
    override fun getItemsList(): Flow<List<SearchResultItem>> = localData
        .getItemsList()
        .flowOn(Dispatchers.IO)

    override suspend fun insertItem(item: SearchResultItem) = localData.insertItem(item)

    override suspend fun deleteItem(item: SearchResultItem) = localData.deleteItem(item)
}
