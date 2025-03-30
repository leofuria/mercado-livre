package br.com.bitsolutions.mercadolivre.data.detail.remote

import br.com.bitsolutions.mercadolivre.data.base.BaseRemoteData
import br.com.bitsolutions.mercadolivre.data.detail.api.DetailApiClient
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DetailRemoteDataImpl(
    private val apiClient: DetailApiClient,
) : BaseRemoteData(), DetailRemoteData {
    override fun getProductItem(productId: String): Flow<SearchResultItem> = flow {
        safeCall {
            val response = apiClient.getProductItem(productId)

            threatResponse(response) {
                emit(it.map { it.toDetailItem() }.first())
            }
        }
    }
}
