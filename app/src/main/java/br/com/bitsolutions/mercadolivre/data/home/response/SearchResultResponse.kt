package br.com.bitsolutions.mercadolivre.data.home.response

import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import com.google.gson.annotations.SerializedName

data class SearchResultResponse(
    @SerializedName("results")
    val items: List<SearchResultItemResponse>,
    val query: String,
    val paging: SearchResultPagingResponse,
) {
    fun toSearchResult(): SearchResult = SearchResult(
        items.map { it.toSearchResultItem() },
        query,
        paging.total,
        paging.primaryResults,
        paging.offset,
        paging.limit,
    )
}
