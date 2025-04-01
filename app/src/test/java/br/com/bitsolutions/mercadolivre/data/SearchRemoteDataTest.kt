package br.com.bitsolutions.mercadolivre.data

import br.com.bitsolutions.mercadolivre.base.BaseTest
import br.com.bitsolutions.mercadolivre.data.home.api.SearchApiClient
import br.com.bitsolutions.mercadolivre.data.home.remote.SearchRemoteDataImpl
import br.com.bitsolutions.mercadolivre.domain.base.BaseThrowable
import br.com.bitsolutions.mercadolivre.util.MocksObjects.LIMIT
import br.com.bitsolutions.mercadolivre.util.MocksObjects.OFFSET
import br.com.bitsolutions.mercadolivre.util.MocksObjects.QUERY
import br.com.bitsolutions.mercadolivre.util.MocksObjects.SITE_ID
import br.com.bitsolutions.mercadolivre.util.MocksObjects.expectedSearchResultsResponse
import br.com.bitsolutions.mercadolivre.util.MocksObjects.genericErrorResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class SearchRemoteDataTest : BaseTest() {
    private lateinit var remoteDataToFlow: SearchRemoteDataImpl

    @Mock
    lateinit var clientToFlow: SearchApiClient

    override fun initialize() {
        super.initialize()

        remoteDataToFlow = SearchRemoteDataImpl(clientToFlow)
    }

    @Test
    fun `Should search result from API`() = runTest {
        Mockito.`when`(
            clientToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT),
        ).thenReturn(expectedSearchResultsResponse)

        remoteDataToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT, null).collect { result ->
            assertEquals(result, expectedSearchResultsResponse.body()?.toSearchResult())
        }

        Mockito.verify(clientToFlow).getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)
    }

    @Test
    fun `Should FAIL search result from API - Response error`() = runTest {
        Mockito.`when`(
            clientToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT),
        ).thenReturn(genericErrorResponse())

        remoteDataToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT, null).catch {
            assertEquals(it is BaseThrowable, true)
            assertEquals(it.cause is Exception, true)
        }.collect { }

        Mockito.verify(clientToFlow).getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)
    }
}
