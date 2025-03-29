package br.com.bitsolutions.mercadolivre.data

import br.com.bitsolutions.mercadolivre.base.BaseTest
import br.com.bitsolutions.mercadolivre.data.home.remote.SearchRemoteData
import br.com.bitsolutions.mercadolivre.domain.base.BaseThrowable
import br.com.bitsolutions.mercadolivre.domain.home.SearchRepository
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import br.com.bitsolutions.mercadolivre.util.MocksObjects.LIMIT
import br.com.bitsolutions.mercadolivre.util.MocksObjects.OFFSET
import br.com.bitsolutions.mercadolivre.util.MocksObjects.QUERY
import br.com.bitsolutions.mercadolivre.util.MocksObjects.SITE_ID
import br.com.bitsolutions.mercadolivre.util.MocksObjects.expectedSearchResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class SearchRepositoryTest : BaseTest() {

    @Mock
    lateinit var remoteToFlow: SearchRemoteData

    @Mock
    lateinit var repositoryToFlow: SearchRepository

    @Test
    fun `Should return search result`() = runTest {
        val flow = flow { emit(expectedSearchResults.toSearchResult()) }
        Mockito.`when`(remoteToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)).thenReturn(flow)

        val remoteResult = remoteToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)
        Mockito.`when`(
            repositoryToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT),
        ).thenReturn(remoteResult)

        repositoryToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)
            .collect { result ->
                assertEquals(result, expectedSearchResults.toSearchResult())
            }
        Mockito.verify(repositoryToFlow).getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)
    }

    @Test
    fun `Should FAIL search result - Exception thrown`() = runTest {
        val expected: Flow<SearchResult> = flow {
            throw BaseThrowable()
        }
        Mockito.`when`(remoteToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)).thenReturn(expected)
        val remoteResult = remoteToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)
        Mockito.`when`(repositoryToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)).thenReturn(remoteResult)

        repositoryToFlow.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)
            .catch {
                assertTrue(it is BaseThrowable)
            }
            .collect()

        Mockito.verify(repositoryToFlow).getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)
    }
}
