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
    @SerializedName("base_price")
    val basePrice: Double?,
    val installments: SearchItemPriceInstallmentsResponse?,
    val shipping: SearchItemShippingResponse,
    val permalink: String,
    val attributes: List<SearchItemAttributeResponse>,
    val seller: SearchItemSellerResponse?,
    val pictures: List<SearchResultItemPictures>?,
) {
    fun toSearchResultItem(): SearchResultItem = SearchResultItem(
        id,
        title,
        thumbnail,
        condition,
        SearchItemPrice(
            price,
            originalPrice ?: basePrice,
            currency,
            price.toBigDecimal().formatMoney(currencyCode = currency),
            originalPrice?.toBigDecimal()?.formatMoney(2, currency) ?: (basePrice?.toBigDecimal()?.formatMoney(2, currency) ?: ""),
            installments?.toSearchItemPriceInstallments(),
        ),
        shipping.freeShipping,
        permalink,
        attributes.map { it.toSearchItemAttribute() },
        seller?.nickname ?: "",
        pictures?.map { it.url },
    )
}
