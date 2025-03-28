package br.com.bitsolutions.mercadolivre

import android.app.Application
import android.content.Context
import br.com.bitsolutions.mercadolivre.di.networkModule
import br.com.bitsolutions.mercadolivre.ui.home.homeModule
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.request.crossfade
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ProjectApplication : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@ProjectApplication)
            modules(
                listOf(
                    networkModule,
                    homeModule,
                ),
            )
        }
    }

    override fun newImageLoader(context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
}
