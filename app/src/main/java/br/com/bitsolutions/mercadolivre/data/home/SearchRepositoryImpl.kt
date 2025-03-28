package br.com.bitsolutions.mercadolivre.data.home

import br.com.bitsolutions.mercadolivre.data.home.remote.SearchRemoteData
import br.com.bitsolutions.mercadolivre.domain.home.SearchRepository
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class SearchRepositoryImpl(
    private val remoteData: SearchRemoteData,
) : SearchRepository {
    override fun getSearchResult(siteId: String, query: String, offset: Int, limit: Int): Flow<SearchResult> = remoteData
        .getSearchResult(siteId, query, offset, limit)
        .flowOn(Dispatchers.IO)
}
