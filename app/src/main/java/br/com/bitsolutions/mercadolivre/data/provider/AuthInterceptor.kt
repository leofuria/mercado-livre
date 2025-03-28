package br.com.bitsolutions.mercadolivre.data.provider

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // TODO move key to env system
        val accessToken = "TEST-1624327687484173-032713-7f074d636e3ffc3119f698d6beb2abcb-2353718947"
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(request)
    }
}
