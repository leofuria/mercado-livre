package br.com.bitsolutions.mercadolivre.domain.home.model

import java.io.Serializable

data class SearchItemAttribute(
    val id: String,
    val name: String,
    val value: String,
) : Serializable
