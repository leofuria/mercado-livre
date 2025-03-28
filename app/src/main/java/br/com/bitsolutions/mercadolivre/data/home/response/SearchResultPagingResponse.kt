package br.com.bitsolutions.mercadolivre.data.home.response

import com.google.gson.annotations.SerializedName

data class SearchResultPagingResponse(
    val total: Int,
    @SerializedName("primary_results")
    val primaryResults: Int,
    val offset: Int,
    val limit: Int,
)
