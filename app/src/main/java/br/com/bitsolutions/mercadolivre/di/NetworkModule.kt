package br.com.bitsolutions.mercadolivre.di

import br.com.bitsolutions.mercadolivre.BuildConfig
import br.com.bitsolutions.mercadolivre.data.provider.AuthInterceptor
import br.com.bitsolutions.mercadolivre.data.provider.HeadersInterceptor
import br.com.bitsolutions.mercadolivre.data.provider.Host
import br.com.bitsolutions.mercadolivre.data.provider.RemoteClientFactory
import okhttp3.Interceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val REMOTE_CLIENT_FACTORY = "RemoteClientFactory"

val networkModule = module {

    single(named(REMOTE_CLIENT_FACTORY)) {
        RemoteClientFactory(
            Host.fromBuildType(BuildConfig.BUILD_TYPE).baseUrl,
            BuildConfig.DEBUG,
            interceptors(),
        )
    }
}

private fun interceptors(): List<Interceptor> {
    return listOf(AuthInterceptor(), HeadersInterceptor())
}
