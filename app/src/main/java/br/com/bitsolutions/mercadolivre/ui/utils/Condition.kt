package br.com.bitsolutions.mercadolivre.ui.utils

import br.com.bitsolutions.mercadolivre.R
import java.util.Locale

enum class Condition(val resId: Int) {
    NEW(R.string.condition_new),
    USED(R.string.condition_used),
    ;

    companion object {
        fun fromBuildType(condition: String): Condition? {
            return Condition.entries.find { it.name == condition.uppercase(Locale.getDefault()) }
        }
    }
}
