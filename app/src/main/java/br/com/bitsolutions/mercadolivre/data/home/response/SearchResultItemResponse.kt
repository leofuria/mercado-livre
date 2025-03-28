package br.com.bitsolutions.mercadolivre.data.home.response

import br.com.bitsolutions.mercadolivre.data.base.formatMoney
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchItemPrice
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import com.google.gson.annotations.SerializedName

data class SearchResultItemResponse(
    val id: String,
    val title: String,
    val thumbnail: String,
    val condition: String,
    val price: Double,
    @SerializedName("currency_id")
    val currency: String,
    @SerializedName("original_price")
    val originalPrice: Double?,
    val installments: SearchItemPriceInstallmentsResponse?,
    val shipping: SearchItemShippingResponse,
    val officialStore: String?,
    val permalink: String,
    val attributes: List<SearchItemAttributeResponse>,
    val seller: SearchItemSellerResponse,
) {
    fun toSearchResultItem(): SearchResultItem = SearchResultItem(
        id,
        title,
        thumbnail.replace("http://", "https://"),
        condition,
        SearchItemPrice(
            price,
            originalPrice,
            currency,
            price.toBigDecimal().formatMoney(currencyCode = currency),
            originalPrice?.toBigDecimal()?.formatMoney(2, currency) ?: "",
            installments?.toSearchItemPriceInstallments(),
        ),
        shipping.freeShipping,
        officialStore,
        permalink,
        attributes.map { it.toSearchItemAttribute() },
        seller.nickname,
    )
}
