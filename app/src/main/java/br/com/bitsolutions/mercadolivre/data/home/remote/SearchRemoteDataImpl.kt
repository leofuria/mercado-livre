package br.com.bitsolutions.mercadolivre.data.home.remote

import br.com.bitsolutions.mercadolivre.data.base.BaseRemoteData
import br.com.bitsolutions.mercadolivre.data.home.api.SearchApiClient
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRemoteDataImpl(
    private val apiClient: SearchApiClient,
) : BaseRemoteData(), SearchRemoteData {
    override fun getSearchResult(siteId: String, query: String, offset: Int, limit: Int): Flow<SearchResult> = flow {
        safeCall {
            val response = apiClient.getSearchResult(siteId, query, offset, limit)

            threatResponse(response) {
                emit(it.toSearchResult())
            }
        }
    }
}
