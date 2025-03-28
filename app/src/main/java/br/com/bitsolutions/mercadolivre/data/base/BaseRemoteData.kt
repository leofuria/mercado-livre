package br.com.bitsolutions.mercadolivre.data.base

import br.com.bitsolutions.mercadolivre.domain.base.BaseThrowable
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

open class BaseRemoteData {
    protected var errorBody: String? = null

    protected inline fun <T> threatResponse(response: Response<T>, exec: (T) -> Unit) {
        errorBody = response.errorBody()?.string()

        exec(response.body() ?: throw Exception(errorBody))
    }

    suspend fun <T : Any> safeCall(exec: suspend () -> T): T {
        return coroutineScope {
            try {
                exec()
            } catch (e: Exception) {
                throw BaseThrowable.parseError(parseRemoteError(e))
            }
        }
    }
}
