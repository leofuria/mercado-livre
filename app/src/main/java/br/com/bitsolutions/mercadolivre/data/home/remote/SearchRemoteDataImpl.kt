package br.com.bitsolutions.mercadolivre.data.home.remote

import br.com.bitsolutions.mercadolivre.data.base.BaseRemoteData
import br.com.bitsolutions.mercadolivre.data.home.api.SearchApiClient
import br.com.bitsolutions.mercadolivre.data.home.response.SearchResultResponse
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class SearchRemoteDataImpl(
    private val apiClient: SearchApiClient,
) : BaseRemoteData(), SearchRemoteData {
    override fun getSearchResult(siteId: String, query: String, offset: Int, limit: Int, fileString: String?): Flow<SearchResult> = flow {
        safeCall {
//            val response = apiClient.getSearchResult(siteId, query, offset, limit)

            // TODO foi preciso mockar o resultado da lista pois mesmo com access token a retorno vem 403
            val response = Response.success(
                Gson().fromJson(
                    fileString,
                    SearchResultResponse::class.java,
                ),
            )
            threatResponse(response) {
                emit(it.toSearchResult())
            }
        }
    }
}
