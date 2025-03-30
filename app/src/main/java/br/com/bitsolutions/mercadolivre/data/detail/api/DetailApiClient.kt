package br.com.bitsolutions.mercadolivre.data.detail.api

import br.com.bitsolutions.mercadolivre.data.detail.response.DetailResultResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DetailApiClient {
    @GET("items")
    suspend fun getProductItem(@Query("ids") productId: String): Response<List<DetailResultResponse>>
}
