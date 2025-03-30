package br.com.bitsolutions.mercadolivre.data.detail.remote

import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.flow.Flow

interface DetailRemoteData {
    fun getProductItem(productId: String): Flow<SearchResultItem>
}
