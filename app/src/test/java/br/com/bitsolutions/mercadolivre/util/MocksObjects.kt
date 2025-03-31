package br.com.bitsolutions.mercadolivre.util

import br.com.bitsolutions.mercadolivre.data.home.response.SearchResultResponse
import br.com.bitsolutions.mercadolivre.domain.base.BaseThrowable
import br.com.bitsolutions.mercadolivre.domain.base.Error.GENERIC_ERROR
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchItemPrice
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

object MocksObjects {

    const val SITE_ID = "MLB"
    const val QUERY = "notebook"
    const val OFFSET = 0
    const val LIMIT = 50

    val genericErrorThrowable = BaseThrowable(GENERIC_ERROR, cause = Throwable("Error response"))

    fun <T> genericErrorResponse(): Response<T> = Response.error(500, "".toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull()))

    val expectedSearchResults = Gson().fromJson(
        UtilsTests.getJson("json/responseSuccessForGetSearchResults.json"),
        SearchResultResponse::class.java,
    )!!

    val expectedSearchResult = SearchResult(
        items = listOf(
            SearchResultItem(
                id = "MLA810645375",
                title = "",
                thumbnail = "",
                condition = "",
                price = SearchItemPrice(
                    amount = 0.0,
                    originalAmount = null,
                    currency = "",
                    formatedAmount = "",
                    formatedOriginalAmount = "",
                    installments = null,
                ),
                freeShipping = false,
                permalink = "",
                attributes = listOf(),
                seller = "",
                pictures = null,
            ),
        ),
        query = "",
        total = 916,
        primaryResults = 916,
        offset = 0,
        limit = 10,
    )

    val expectedSearchResultsResponse: Response<SearchResultResponse> = Response.success(expectedSearchResults)
}
