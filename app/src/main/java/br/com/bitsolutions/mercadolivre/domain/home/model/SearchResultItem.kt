package br.com.bitsolutions.mercadolivre.domain.home.model

import java.io.Serializable

data class SearchResultItem(
    val id: String,
    val title: String,
    val thumbnail: String,
    val condition: String,
    val price: SearchItemPrice,
    val freeShipping: Boolean,
    val permalink: String,
    val attributes: List<SearchItemAttribute>,
    val seller: String,
    val pictures: List<String>?,
    var isFavorite: Boolean = false,
) : Serializable
