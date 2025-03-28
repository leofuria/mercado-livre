package br.com.bitsolutions.mercadolivre.data.base

import br.com.bitsolutions.mercadolivre.domain.base.BaseThrowable
import br.com.bitsolutions.mercadolivre.domain.base.Error
import br.com.bitsolutions.mercadolivre.domain.base.model.ErrorApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.math.BigDecimal
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import javax.net.ssl.SSLHandshakeException
import retrofit2.HttpException

val moshi: Moshi by lazy { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

fun parseRemoteError(error: Throwable): BaseThrowable {
    val (errorType, message) = when (error) {
        is ConnectException,
        is UnknownHostException,
        -> Pair<Enum<*>, String?>(Error.UNKNOWN_HOST, parseJsonError(error.message))
        is SSLHandshakeException -> Pair<Enum<*>, String?>(Error.SSL_HANDSHAKE, error.message)
        is HttpException -> {
            Pair(
                extractErrorTypeFromHttpCode(error.code()),
                parseJsonError(error.response()?.errorBody()?.string()),
            )
        }
        else -> Pair(Error.GENERIC_ERROR_ON_PARSE, null)
    }

    return BaseThrowable(errorType, message, error)
}

private fun extractErrorTypeFromHttpCode(httpCode: Int): Enum<*> {
    return when (httpCode) {
        401 -> Error.INVALID_LOGIN
        412 -> Error.INACTIVE_ACCOUNT
        500 -> Error.POST_CANCELLATION_INTERNAL_ERROR
        else -> Error.GENERIC_ERROR_EXTRACT_HTTP_EXCEPTION
    }
}

private fun parseJsonError(error: String?): String? {
    if (error == null) return null

    return try {
        moshi.adapter(ErrorApi::class.java)
            .fromJson(error)?.errors?.takeIf { it.isNotEmpty() }?.get(0)
    } catch (error: Exception) {
        null
    }
}

fun BigDecimal.formatMoney(maximumFractionDigits: Int = 0, currencyCode: String? = "BRL"): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    numberFormat.maximumFractionDigits = maximumFractionDigits
    val currency = Currency.getInstance(currencyCode)
    numberFormat.currency = currency
    return numberFormat.format(this)
}
