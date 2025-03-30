package br.com.bitsolutions.mercadolivre.ui.detail

import br.com.bitsolutions.mercadolivre.data.detail.DetailRepositoryImpl
import br.com.bitsolutions.mercadolivre.data.detail.api.DetailApiClient
import br.com.bitsolutions.mercadolivre.data.detail.remote.DetailRemoteData
import br.com.bitsolutions.mercadolivre.data.detail.remote.DetailRemoteDataImpl
import br.com.bitsolutions.mercadolivre.data.provider.RemoteClientFactory
import br.com.bitsolutions.mercadolivre.di.REMOTE_CLIENT_FACTORY
import br.com.bitsolutions.mercadolivre.domain.detail.DetailRepository
import br.com.bitsolutions.mercadolivre.domain.detail.usecase.DetailUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val detailModule = module {

    viewModelOf(::DetailViewModel)

    singleOf(::DetailUseCase)

    singleOf(::DetailRepositoryImpl).bind(DetailRepository::class)

    singleOf(::DetailRemoteDataImpl).bind(DetailRemoteData::class)

    single { get<RemoteClientFactory>(named(REMOTE_CLIENT_FACTORY)).createClient(DetailApiClient::class) }
}
