package br.com.bitsolutions.mercadolivre.presentation

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import br.com.bitsolutions.mercadolivre.base.BaseTest
import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.home.usecase.SearchUseCase
import br.com.bitsolutions.mercadolivre.ui.home.HomeViewModel
import br.com.bitsolutions.mercadolivre.util.CoroutinesTestRule
import br.com.bitsolutions.mercadolivre.util.MocksObjects.LIMIT
import br.com.bitsolutions.mercadolivre.util.MocksObjects.OFFSET
import br.com.bitsolutions.mercadolivre.util.MocksObjects.QUERY
import br.com.bitsolutions.mercadolivre.util.MocksObjects.SITE_ID
import br.com.bitsolutions.mercadolivre.util.MocksObjects.expectedSearchResults
import br.com.bitsolutions.mercadolivre.util.MocksObjects.genericErrorThrowable
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest : BaseTest() {

    @Mock
    private lateinit var searchUseCase: SearchUseCase

    @get:Rule
    val coroutineRule = CoroutinesTestRule()

    private lateinit var homeViewModel: HomeViewModel

    override fun initialize() {
        super.initialize()
//        searchUseCase = SearchUseCase(repository)
        homeViewModel = HomeViewModel(searchUseCase)
    }

    @Test
    fun searchResultItems_stateInitial_stateIsIdle() = runTest {
        homeViewModel.resultItems.test {
            assertEquals(State.Idle, awaitItem())
        }
    }

    @Test
    fun searchResultItems_sendQueryParams_passForLoading() = runTest {
        doReturn(
            flowOf(
                State.Loading,
            ),
        ).`when`(searchUseCase).execute(SITE_ID, QUERY, OFFSET, LIMIT)

        turbineScope {
            homeViewModel.resultItems.test {
                homeViewModel.getSearchResult(SITE_ID, QUERY, OFFSET)

                val turbine1 = flowOf(State.Idle).testIn(backgroundScope)
                val turbine2 = flowOf(State.Loading).testIn(backgroundScope)

                assertEquals(State.Idle, turbine1.awaitItem())
                assertEquals(State.Loading, turbine2.awaitItem())

                turbine1.awaitComplete()
                turbine2.awaitComplete()

                cancelAndIgnoreRemainingEvents()
            }
        }

        verify(searchUseCase).execute(SITE_ID, QUERY, OFFSET, LIMIT)
    }

    @Test
    fun searchResultItems_sendQueryParams_returnResultItems() = runTest {
        doReturn(
            flowOf(
                State.data(expectedSearchResults.toSearchResult()),
            ),
        ).`when`(searchUseCase).execute(SITE_ID, QUERY, OFFSET, LIMIT)

        turbineScope {
            homeViewModel.resultItems.test {
                homeViewModel.getSearchResult(SITE_ID, QUERY, OFFSET)

                val turbine1 = flowOf(State.Idle).testIn(backgroundScope)
                val turbine2 = flowOf(State.Loading).testIn(backgroundScope)
                val turbine3 = flowOf(State.data(expectedSearchResults.toSearchResult())).testIn(backgroundScope)

                assertEquals(State.Idle, turbine1.awaitItem())
                assertEquals(State.Loading, turbine2.awaitItem())
                assertEquals(State.data(expectedSearchResults.toSearchResult()), turbine3.awaitItem())

                turbine1.awaitComplete()
                turbine2.awaitComplete()
                turbine3.awaitComplete()

                cancelAndIgnoreRemainingEvents()
            }
        }

        verify(searchUseCase).execute(SITE_ID, QUERY, OFFSET, LIMIT)
    }

    @Test
    fun searchResultItems_sendQueryParams_returnError() = runTest {
        val error = State.error<Nothing>(genericErrorThrowable.type, genericErrorThrowable.cause)
        doReturn(
            flowOf(error),
        ).`when`(searchUseCase).execute(SITE_ID, QUERY, OFFSET, LIMIT)

        homeViewModel.getSearchResult(SITE_ID, QUERY, OFFSET)

        turbineScope {
            homeViewModel.resultItems.test {
                val turbine1 = flowOf(State.Idle).testIn(backgroundScope)
                val turbine2 = flowOf(State.Loading).testIn(backgroundScope)
                val turbine3 = flowOf(error).testIn(backgroundScope)

                assertEquals(State.Idle, turbine1.awaitItem())
                assertEquals(State.Loading, turbine2.awaitItem())
                assertEquals((error), turbine3.awaitItem())

                turbine1.awaitComplete()
                turbine2.awaitComplete()
                turbine3.awaitComplete()

                cancelAndIgnoreRemainingEvents()
            }
        }

        verify(searchUseCase).execute(SITE_ID, QUERY, OFFSET, LIMIT)
    }
}
