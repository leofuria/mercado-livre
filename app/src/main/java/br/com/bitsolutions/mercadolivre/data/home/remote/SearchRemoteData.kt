package br.com.bitsolutions.mercadolivre.data.home.remote

import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRemoteData {
    fun getSearchResult(siteId: String, query: String, offset: Int, limit: Int, fileString: String?): Flow<SearchResult>
}
