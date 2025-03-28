package br.com.bitsolutions.mercadolivre.domain.home.model

import java.io.Serializable

data class SearchItemPrice(
    val amount: Double,
    val originalAmount: Double?,
    val currency: String,
    val formatedAmount: String,
    val formatedOriginalAmount: String,
    val installments: SearchItemPriceInstallments?,
) : Serializable
