package br.com.bitsolutions.mercadolivre.data.favorite.local

import br.com.bitsolutions.mercadolivre.data.database.AppDataBase
import br.com.bitsolutions.mercadolivre.data.favorite.local.entity.ItemEntity
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteLocalDataImpl(
    private val appDatabase: AppDataBase,
) : FavoriteLocalData {
    override fun getItemsList(): Flow<List<SearchResultItem>> = flow {
        emit(
            appDatabase.itemDao().getItemsList()?.map { it.toSearchResultItem() } ?: emptyList(),
        )
    }

    override suspend fun insertItem(item: SearchResultItem) = appDatabase.itemDao().insertItem(ItemEntity.fromSearchResultItem(item))

    override suspend fun deleteItem(item: SearchResultItem) = appDatabase.itemDao().deleteItem(ItemEntity.fromSearchResultItem(item))
}
