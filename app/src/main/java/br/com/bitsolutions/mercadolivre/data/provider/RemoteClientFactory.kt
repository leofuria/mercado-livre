package br.com.bitsolutions.mercadolivre.data.provider

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.reflect.KClass
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RemoteClientFactory(
    baseUrl: String,
    debug: Boolean = false,
    interceptors: List<Interceptor> = emptyList(),
    connectTimeout: Long = 10,
    writeTimeout: Long = 10,
    readTimeout: Long = 30,
) {
    val gson: Gson = GsonBuilder()
        .disableHtmlEscaping()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS")
        .setLenient()
        .create()

    private val logger = HttpLoggingInterceptor().setLevel(BODY)

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .apply { interceptors.iterator().forEach { addInterceptor(it) } }
        .apply { if (debug) addInterceptor(logger) }
        .connectTimeout(connectTimeout, SECONDS)
        .writeTimeout(writeTimeout, SECONDS)
        .readTimeout(readTimeout, SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(okHttpClient)
        .build()

    fun <T : Any> createClient(serviceClass: KClass<T>): T = retrofit.create(serviceClass.java)
}
