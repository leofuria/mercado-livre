package br.com.bitsolutions.mercadolivre.base

import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.mockito.MockitoAnnotations

abstract class BaseTest {
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        MockKAnnotations.init(this)
        initialize()
    }

    @After
    fun shutdown() {
        unmockkAll()
        finish()
    }

    open fun initialize() {}

    open fun finish() {}
}
