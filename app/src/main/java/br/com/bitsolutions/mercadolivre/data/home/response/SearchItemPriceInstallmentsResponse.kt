package br.com.bitsolutions.mercadolivre.data.home.response

import br.com.bitsolutions.mercadolivre.data.base.formatMoney
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchItemPriceInstallments
import com.google.gson.annotations.SerializedName

data class SearchItemPriceInstallmentsResponse(
    val quantity: Int,
    val amount: Double,
    @SerializedName("currency_id")
    val currency: String,
    val rate: Double,
) {
    fun toSearchItemPriceInstallments(): SearchItemPriceInstallments = SearchItemPriceInstallments(
        quantity,
        amount,
        currency,
        rate,
        amount.toBigDecimal().formatMoney(2, currency),
    )
}
