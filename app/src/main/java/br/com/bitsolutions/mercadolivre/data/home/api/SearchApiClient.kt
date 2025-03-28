package br.com.bitsolutions.mercadolivre.data.home.api

import br.com.bitsolutions.mercadolivre.data.home.response.SearchResultResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApiClient {
    @GET("sites/{siteID}/search")
    suspend fun getSearchResult(
        @Path("siteID") siteID: String,
        @Query("q") text: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Response<SearchResultResponse>
}
