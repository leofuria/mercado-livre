package br.com.bitsolutions.mercadolivre.data.detail.response

import br.com.bitsolutions.mercadolivre.data.home.response.SearchResultItemResponse
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import com.google.gson.annotations.SerializedName

data class DetailResultResponse(
    @SerializedName("body")
    val item: SearchResultItemResponse,
) {
    fun toDetailItem(): SearchResultItem = item.toSearchResultItem()
}
