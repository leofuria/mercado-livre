package br.com.bitsolutions.mercadolivre.domain.home.model

import java.io.Serializable

data class SearchResult(
    val items: List<SearchResultItem>,
    val query: String,
    val total: Int,
    val primaryResults: Int,
    val offset: Int,
    val limit: Int,
) : Serializable
