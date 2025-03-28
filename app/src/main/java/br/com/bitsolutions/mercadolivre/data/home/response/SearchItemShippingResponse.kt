package br.com.bitsolutions.mercadolivre.data.home.response

import com.google.gson.annotations.SerializedName

data class SearchItemShippingResponse(
    @SerializedName("free_shipping")
    val freeShipping: Boolean,
)
