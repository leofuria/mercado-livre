package br.com.bitsolutions.mercadolivre.domain

import br.com.bitsolutions.mercadolivre.base.BaseTest
import br.com.bitsolutions.mercadolivre.domain.base.BaseThrowable
import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.home.SearchRepository
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import br.com.bitsolutions.mercadolivre.domain.home.usecase.SearchUseCase
import br.com.bitsolutions.mercadolivre.util.MocksObjects.LIMIT
import br.com.bitsolutions.mercadolivre.util.MocksObjects.OFFSET
import br.com.bitsolutions.mercadolivre.util.MocksObjects.QUERY
import br.com.bitsolutions.mercadolivre.util.MocksObjects.SITE_ID
import br.com.bitsolutions.mercadolivre.util.MocksObjects.expectedSearchResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class SearchUseCaseTest : BaseTest() {

    @Mock
    private lateinit var repository: SearchRepository

    private lateinit var useCase: SearchUseCase

    override fun initialize() {
        super.initialize()

        useCase = SearchUseCase(repository)
    }

    @Test
    fun `Should return search result`() = runTest {
        val expectedFlow = flow { emit(expectedSearchResult) }

        Mockito.`when`(repository.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)).thenReturn(expectedFlow)

        val result = useCase.execute(SITE_ID, QUERY, OFFSET, LIMIT).toList()

        Assert.assertEquals(State.Loading, result[0])
        Assert.assertTrue(result[1] is State.Data)

        val data = (result[1] as State.Data).data

        Assert.assertEquals(1, data.items.size)
        Assert.assertEquals(expectedSearchResult, data)
        Mockito.verify(repository).getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)
    }

    @Test
    fun `Should FAIL search result request error`() = runTest {
        val expectedFlow = flow<SearchResult> { throw BaseThrowable() }

        Mockito.`when`(repository.getSearchResult(SITE_ID, QUERY, OFFSET, LIMIT)).thenReturn(expectedFlow)

        val result = useCase.execute(SITE_ID, QUERY, OFFSET, LIMIT).toList()

        Assert.assertEquals(State.Loading, result[0])
        Assert.assertTrue(result[1] is State.Error)
    }
}
