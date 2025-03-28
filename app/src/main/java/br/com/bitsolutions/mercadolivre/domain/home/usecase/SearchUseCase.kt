package br.com.bitsolutions.mercadolivre.domain.home.usecase

import br.com.bitsolutions.mercadolivre.domain.base.BaseThrowable
import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.home.SearchRepository
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class SearchUseCase(private val repository: SearchRepository) {
    fun execute(siteId: String, query: String, offset: Int, limit: Int): Flow<State<SearchResult>> {
        return getSearchResult(siteId, query, offset, limit).onStart {
            emit(State.loading())
        }
    }

    private fun getSearchResult(siteId: String, query: String, offset: Int, limit: Int): Flow<State<SearchResult>> {
        return repository.getSearchResult(siteId, query, offset, limit)
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
