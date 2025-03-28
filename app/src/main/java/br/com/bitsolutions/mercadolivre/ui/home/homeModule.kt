package br.com.bitsolutions.mercadolivre.ui.home

import br.com.bitsolutions.mercadolivre.data.home.SearchRepositoryImpl
import br.com.bitsolutions.mercadolivre.data.home.api.SearchApiClient
import br.com.bitsolutions.mercadolivre.data.home.remote.SearchRemoteData
import br.com.bitsolutions.mercadolivre.data.home.remote.SearchRemoteDataImpl
import br.com.bitsolutions.mercadolivre.data.provider.RemoteClientFactory
import br.com.bitsolutions.mercadolivre.di.REMOTE_CLIENT_FACTORY
import br.com.bitsolutions.mercadolivre.domain.home.SearchRepository
import br.com.bitsolutions.mercadolivre.domain.home.usecase.SearchUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {

    viewModelOf(::HomeViewModel)

    singleOf(::SearchUseCase)

    singleOf(::SearchRepositoryImpl).bind(SearchRepository::class)

    singleOf(::SearchRemoteDataImpl).bind(SearchRemoteData::class)

    single { get<RemoteClientFactory>(named(REMOTE_CLIENT_FACTORY)).createClient(SearchApiClient::class) }
}
