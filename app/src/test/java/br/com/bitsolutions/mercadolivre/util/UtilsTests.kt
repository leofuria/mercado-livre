package br.com.bitsolutions.mercadolivre.util

import java.io.File

object UtilsTests {
    fun getJson(path: String): String {
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path ?: "")
        return String(file.readBytes())
    }
}
