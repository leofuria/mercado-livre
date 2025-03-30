package br.com.bitsolutions.mercadolivre.domain.detail

import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.flow.Flow

interface DetailRepository {
    fun getProductItem(productId: String): Flow<SearchResultItem>
}
