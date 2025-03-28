package br.com.bitsolutions.mercadolivre.data.provider

import br.com.bitsolutions.mercadolivre.BuildConfig
import java.io.IOException
import okhttp3.Headers.Builder
import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor() : Interceptor {

    val USER_AGENT = String.format(
        "Project Android(%s)/%s(%s);",
        BuildConfig.APPLICATION_ID,
        BuildConfig.VERSION_NAME,
        BuildConfig.VERSION_CODE,
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val headersBuilder = originalRequest.headers.newBuilder()

        headersBuilder.addIfNotPresent("User-Agent", USER_AGENT)

        val requestBuilder = originalRequest.newBuilder()
        requestBuilder.headers(headersBuilder.build())

        var response: Response? = null
        var exception: IOException? = null
        var tryCount = 0
        val maxTryCount = 3

        do {
            try {
                response = chain.proceed(requestBuilder.build())
            } catch (error: IOException) {
                exception = error
            } finally {
                tryCount++
            }
        } while (tryCount <= maxTryCount && (response == null || !response.isSuccessful))

        if (response == null && exception != null) throw exception

        return response!!
    }

    fun Builder.addIfNotPresent(name: String, value: String) {
        if (name.isEmpty() || value.isEmpty()) return

        if (this[name] == null) {
            this.add(name, value)
        }
    }
}
