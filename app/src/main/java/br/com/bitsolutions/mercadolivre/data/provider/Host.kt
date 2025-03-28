package br.com.bitsolutions.mercadolivre.data.provider

import java.util.*

enum class Host(val baseUrl: String) {
    RELEASE("https://api.mercadolibre.com/"),
    DEBUG("https://api.mercadolibre.com/"),
    ;

    companion object {
        fun fromBuildType(flavor: String, buildType: String): Host {
            return valueOf("${flavor}_$buildType".uppercase(Locale.getDefault()))
        }
        fun fromBuildType(buildType: String): Host {
            return valueOf(buildType.uppercase(Locale.getDefault()))
        }
    }
}
