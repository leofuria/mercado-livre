package br.com.bitsolutions.mercadolivre.domain.base

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

class BaseThrowable(
    val type: Enum<*> = Error.GENERIC_ERROR,

    message: String? = null,

    cause: Throwable? = null,
) : Throwable(message, cause) {

    fun <T> toSingleError(): Single<T> = Single.error(this)

    fun toCompletableError(): Completable = Completable.error(this)

    fun toObservableError(): Observable<Error> = Observable.error(this)

    fun toFlowableError(): Flowable<Error> = Flowable.error(this)

    companion object {
        fun parseError(error: Throwable): BaseThrowable {
            return if (error is BaseThrowable) {
                error
            } else {
                BaseThrowable(Error.GENERIC_ERROR, cause = error)
            }
        }
    }
}
