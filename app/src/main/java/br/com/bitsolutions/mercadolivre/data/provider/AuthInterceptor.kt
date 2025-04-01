package br.com.bitsolutions.mercadolivre.data.provider

import br.com.bitsolutions.mercadolivre.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.ACCESS_TOKEN}")
            .build()

        return chain.proceed(request)
    }
}
