package br.com.bitsolutions.mercadolivre.data.detail

import br.com.bitsolutions.mercadolivre.data.detail.remote.DetailRemoteData
import br.com.bitsolutions.mercadolivre.domain.detail.DetailRepository
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DetailRepositoryImpl(
    private val remoteData: DetailRemoteData,
) : DetailRepository {
    override fun getProductItem(productId: String): Flow<SearchResultItem> = remoteData
        .getProductItem(productId)
        .flowOn(Dispatchers.IO)
}
