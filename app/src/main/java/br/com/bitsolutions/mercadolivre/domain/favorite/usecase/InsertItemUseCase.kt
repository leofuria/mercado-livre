package br.com.bitsolutions.mercadolivre.domain.favorite.usecase

import br.com.bitsolutions.mercadolivre.domain.favorite.FavoriteRepository
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem

class InsertItemUseCase(private val repository: FavoriteRepository) {
    suspend fun execute(item: SearchResultItem) = repository.insertItem(item)
}
