package br.com.bitsolutions.mercadolivre.domain.home.model

import java.io.Serializable

data class SearchItemPriceInstallments(
    val quantity: Int,
    val amount: Double,
    val currency: String,
    val rate: Double,
    val formatedAmount: String,
) : Serializable
