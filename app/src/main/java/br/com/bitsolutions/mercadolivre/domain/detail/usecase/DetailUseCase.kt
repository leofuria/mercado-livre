package br.com.bitsolutions.mercadolivre.domain.detail.usecase

import br.com.bitsolutions.mercadolivre.domain.base.BaseThrowable
import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.detail.DetailRepository
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class DetailUseCase(private val repository: DetailRepository) {
    fun execute(productId: String): Flow<State<SearchResultItem>> {
        return getProductItem(productId).onStart {
            emit(State.loading())
        }
    }

    private fun getProductItem(productId: String): Flow<State<SearchResultItem>> {
        return repository.getProductItem(productId)
            .map { State.data(it) }
            .catch { throwable ->
                emit(
                    (throwable as? BaseThrowable)?.let {
                        State.error(it.type, it)
                    } ?: State.error(throwable),
                )
            }
    }
}
