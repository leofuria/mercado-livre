package br.com.bitsolutions.mercadolivre.domain.home

import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun getSearchResult(siteId: String, query: String, offset: Int, limit: Int): Flow<SearchResult>
}
