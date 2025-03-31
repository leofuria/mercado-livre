package br.com.bitsolutions.mercadolivre.data.home

import br.com.bitsolutions.mercadolivre.data.favorite.local.FavoriteLocalData
import br.com.bitsolutions.mercadolivre.data.home.remote.SearchRemoteData
import br.com.bitsolutions.mercadolivre.domain.home.SearchRepository
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepositoryImpl(
    private val remoteData: SearchRemoteData,
    private val localData: FavoriteLocalData,
) : SearchRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSearchResult(siteId: String, query: String, offset: Int, limit: Int, fileString: String?): Flow<SearchResult> = remoteData
        .getSearchResult(siteId, query, offset, limit, fileString)
        .flatMapLatest { resultList ->
            flow {
                val ids = mutableListOf<String>()

                resultList.items.forEach { item ->
                    ids.add(item.id)
                }

                if (ids.isEmpty()) {
                    emit(resultList)
                } else {
                    localData.getItemsList()
                        .collect { favoriteList ->
                            resultList.items = resultList.items.map { item ->
                                item.copy(
                                    isFavorite = favoriteList.firstOrNull { it.id == item.id }?.let { true } == true,
                                )
                            }
                            emit(resultList)
                        }
                }
            }
        }
        .flowOn(Dispatchers.IO)
}
