package br.com.bitsolutions.mercadolivre.ui.favorite

import br.com.bitsolutions.mercadolivre.data.favorite.FavoriteRepositoryImpl
import br.com.bitsolutions.mercadolivre.data.favorite.local.FavoriteLocalData
import br.com.bitsolutions.mercadolivre.data.favorite.local.FavoriteLocalDataImpl
import br.com.bitsolutions.mercadolivre.domain.favorite.FavoriteRepository
import br.com.bitsolutions.mercadolivre.domain.favorite.usecase.DeleteItemUseCase
import br.com.bitsolutions.mercadolivre.domain.favorite.usecase.GetItemsListUseCase
import br.com.bitsolutions.mercadolivre.domain.favorite.usecase.InsertItemUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val favoriteModule = module {

    viewModelOf(::FavoriteViewModel)

    singleOf(::GetItemsListUseCase)

    singleOf(::InsertItemUseCase)

    singleOf(::DeleteItemUseCase)

    singleOf(::FavoriteRepositoryImpl).bind(FavoriteRepository::class)

    singleOf(::FavoriteLocalDataImpl).bind(FavoriteLocalData::class)
}
